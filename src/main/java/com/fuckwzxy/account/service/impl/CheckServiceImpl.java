package com.fuckwzxy.account.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fuckwzxy.account.bean.ApiInfo;
import com.fuckwzxy.account.bean.SignMessage;
import com.fuckwzxy.account.bean.UserInfo;
import com.fuckwzxy.account.common.ApiConstant;
import com.fuckwzxy.account.mapper.ApiMapper;
import com.fuckwzxy.account.mapper.UserMapper;
import com.fuckwzxy.account.service.CheckService;
import com.fuckwzxy.account.util.SendUtil;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

/**
 * @author wzh
 * 2020/9/23 19:05
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Resource
    UserMapper userMapper;

    @Resource
    ApiMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Override
    public void morningCheck() {
        List<UserInfo> userInfoList = userMapper.selectAll();
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(1);

        for (UserInfo userInfo : userInfoList) {
            sendUtil.sendCheckRequest(userInfo, apiInfo, 1);
        }
    }

    @Override
    public void noonCheck() {
        List<UserInfo> userInfoList = userMapper.selectAll();
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(1);

        for (UserInfo userInfo : userInfoList) {
            sendUtil.sendCheckRequest(userInfo, apiInfo, 2);
        }
    }

    @Override
    public void eveningCheck() {
        List<UserInfo> userInfoList = userMapper.selectAll();
        ApiInfo apiInfo = apiInfoMapper.selectByPrimaryKey(1);

        for (UserInfo userInfo : userInfoList) {
            sendUtil.sendCheckRequest(userInfo, apiInfo, 3);
        }
    }

    @Override
    public void singIn() {
        List<UserInfo> userInfoList = userMapper.selectAll();

        ApiInfo getSignMessageApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.GET_SIGN_MESSAGE);
        ApiInfo signApiInfo = apiInfoMapper.selectByPrimaryKey(ApiConstant.DO_SIGN);


        for (UserInfo userInfo : userInfoList) {
            String body = sendUtil.GetJSON(userInfo,getSignMessageApiInfo);
            JSONObject data =  null;
            if(!JSONUtil.parseObj(body).containsKey("data") ) {
                continue;
            }else{
                data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);
                if(Integer.parseInt(data.get("state").toString()) == 1) {
                    continue;
                }
            }

            SignMessage signMessage = new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
            sendUtil.sendSignRequest(userInfo, signApiInfo, signMessage);
        }
    }

}
