package com.fuckwzxy.account.service;

import com.fuckwzxy.account.bean.UserInfo;

public interface UserService  {
    void addUser(UserInfo userInfo);


    /**
     * 修改用户
     * @param userInfo 值
     * @return 结果字符串
     */
    String updateUser(UserInfo userInfo);
}
