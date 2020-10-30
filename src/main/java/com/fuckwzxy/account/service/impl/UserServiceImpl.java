package com.fuckwzxy.account.service.impl;

import com.fuckwzxy.account.bean.UserInfo;
import com.fuckwzxy.account.mapper.UserMapper;
import com.fuckwzxy.account.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void addUser(UserInfo userInfo) {
        userMapper.insert(userInfo);
    }

    @Override
    public String updateUser(UserInfo userInfoUpdateVo) {
        userMapper.updateByPrimaryKey(userInfoUpdateVo);
        return "success";
    }


}
