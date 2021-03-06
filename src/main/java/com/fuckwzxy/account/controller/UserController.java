package com.fuckwzxy.account.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fuckwzxy.account.bean.ApiInfo;
import com.fuckwzxy.account.bean.SignMessage;
import com.fuckwzxy.account.bean.UserInfo;
import com.fuckwzxy.account.common.ApiConstant;
import com.fuckwzxy.account.common.Result;
import com.fuckwzxy.account.common.ResultCode;
import com.fuckwzxy.account.common.ResultFactory;
import com.fuckwzxy.account.mapper.ApiMapper;
import com.fuckwzxy.account.mapper.UserMapper;
import com.fuckwzxy.account.service.UserService;

import com.fuckwzxy.account.util.SendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @PostMapping("/register")
    public  Result addManager(@RequestBody UserInfo userInfo){

        UserInfo uif = userMapper.selectByPrimaryKey(userInfo.getId());
        if(uif != null){
            return ResultFactory.fail(ResultCode.USER_PRESENCE);
        }


        userService.addUser(userInfo);
        Map<Object, Object> map = MapBuilder.create()
                .put("id", userInfo.getId())
                .put("name", userInfo.getName())
                .put("password", userInfo.getPassword())
                .map();
        return ResultFactory.success(map,ResultCode.OK);
    }

    @PostMapping("/login")
    public Result LoginAndUpdate(@RequestBody UserInfo uservo) throws ParseException {

        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("id", uservo.getId()).andEqualTo("password", uservo.getPassword());
        UserInfo userInfo = userMapper.selectOneByExample(example);

        if(userInfo == null ){
            return ResultFactory.fail(ResultCode.IncorrectCredentialsException);
        }else{
            //userInfo.setToken(uservo.getToken());

            int status = userMapper.updateByPrimaryKeySelective(uservo);
            if(status == 0) return  ResultFactory.fail(ResultCode.UPDATE_FAIL);

            helper(userInfo);

            Map<Object, Object> map = MapBuilder.create()
                    .put("id", userInfo.getId())
                    .put("name",userInfo.getName())
                    .put("password", userInfo.getPassword())
                    .put("token", userInfo.getToken())
                    .map();
            return ResultFactory.success(map, ResultCode.OK);
        }
    }

    public void helper(UserInfo userInfo) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        Date now = df.parse(df.format(new Date()));

        if(belongCalendar(now, df.parse("00:01"), df.parse("05:00"))){
            JudgeAndDo(userInfo,1);
        }else if(belongCalendar(now, df.parse("11:01"), df.parse("14:59"))){
            JudgeAndDo(userInfo,2);
        }else if(belongCalendar(now, df.parse("17:01"), df.parse("20:59"))){
            JudgeAndDo(userInfo,3);
        }
        if(belongCalendar(now, df.parse("20:06"), df.parse("21:59"))){
            JudgeAndSign(userInfo);
        }

    }

    public void JudgeAndDo(UserInfo userInfo,int seq){
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.NEED_CHECK);//获取三检状况API
        if (sendUtil.needCheck(apiInfo,userInfo, seq)) {
            sendUtil.sendCheckRequest(userInfo, apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_CHECK), seq);
        }
    }

    public void JudgeAndSign(UserInfo userInfo){
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.GET_SIGN_MESSAGE);//获取三检状况API
        String body = sendUtil.GetJSON(userInfo,apiInfo);

        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);

        if(Integer.parseInt(data.get("state").toString()) == 1){
            SignMessage signMessage = new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
            ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);//获取晚签到API
            sendUtil.sendSignRequest(userInfo,signApiInfo,signMessage);
        }
    }

    //https://blog.csdn.net/finaly_yu/article/details/87632726
    public boolean belongCalendar(Date nowTime, Date beginTime,Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

}
