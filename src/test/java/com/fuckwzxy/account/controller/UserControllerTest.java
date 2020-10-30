package com.fuckwzxy.account.controller;

import cn.hutool.core.map.MapBuilder;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fuckwzxy.account.bean.ApiInfo;
import com.fuckwzxy.account.bean.SignMessage;
import com.fuckwzxy.account.bean.UserInfo;
import com.fuckwzxy.account.common.ApiConstant;
import com.fuckwzxy.account.common.ResultCode;
import com.fuckwzxy.account.common.ResultFactory;
import com.fuckwzxy.account.mapper.ApiMapper;
import com.fuckwzxy.account.mapper.UserMapper;
import com.fuckwzxy.account.service.UserService;
import com.fuckwzxy.account.util.SendUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserControllerTest {
    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Test
    void Test(){
        UserInfo userInfo = new UserInfo("17251104285","","123456","9666150b-91a3-4932-b4d6-54152b7b0a4f",0);


        JudgeAndSign(userInfo);

    }

    @Test
    void addManager() {

        UserInfo userInfo = new UserInfo("17251104285","","123456","7ed683bb-30f3-4cf1-ab5f-d5608c664d98",0);
        UserInfo uif = userMapper.selectByPrimaryKey(userInfo.getId());

        if(uif != null) return ;

        userService.addUser(userInfo);


    }

    @Test
    void loginAndUpdate() throws ParseException {
        UserInfo uservo = new UserInfo("17251104221","","123456","9666150b-91a3-4932-b4d6-54152b7b0a4f",0);

        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("id", uservo.getId()).andEqualTo("password", uservo.getPassword());
        UserInfo userInfo = userMapper.selectOneByExample(example);

        if(userInfo == null ){
            System.out.println("账号密码错误");
        }else {
            //userInfo.setToken(uservo.getToken());
            //JudgeAndSign(uservo);
            helper(userInfo);
        }
    }

    public void helper(UserInfo userInfo) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
        Date now = df.parse(df.format(new Date()));

        if(belongCalendar(now, df.parse("20:06"), df.parse("21:59"))){
            System.out.println("可以晚签到了");
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
        if(!JSONUtil.parseObj(body).containsKey("data")) {
            System.out.println("空指针异常");
            return;
        }else{
            System.out.println("可以获取");
            return;
        }
//        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);
//
//        if(Integer.parseInt(data.get("state").toString()) == 1){
//            SignMessage signMessage = new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
//            ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);//获取晚签到API
//            sendUtil.sendSignRequest(userInfo,signApiInfo,signMessage);
//        }
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