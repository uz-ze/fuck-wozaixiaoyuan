package uz.fuckwzxy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.fuckwzxy.cons.ApiConstant;
import uz.fuckwzxy.entity.ApiInfo;
import uz.fuckwzxy.entity.SignMessage;
import uz.fuckwzxy.entity.UserInfo;
import uz.fuckwzxy.mapper.ApiInfoMapper;
import uz.fuckwzxy.mapper.UserInfoMapper;
import uz.fuckwzxy.service.CheckService;
import uz.fuckwzxy.util.SendUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wzh
 * 2020/9/23 19:05
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Resource
    UserInfoMapper userInfoMapper;

    @Resource
    ApiInfoMapper apiInfoMapper;

    @Autowired
    SendUtil sendUtil;

    @Override
    public void morningCheck() {
        List<UserInfo> userList = userInfoMapper.selectAll();
        ApiInfo api = apiInfoMapper.selectOne(new ApiInfo(ApiConstant.TYPE_CHECK));

        for (UserInfo user : userList) {
            sendUtil.sendCheckRequest(user, api, 1);
        }
    }

    @Override
    public void noonCheck() {
        List<UserInfo> userList = userInfoMapper.selectAll();
        ApiInfo api = apiInfoMapper.selectOne(new ApiInfo(ApiConstant.TYPE_CHECK));

        for (UserInfo user : userList) {
            sendUtil.sendCheckRequest(user, api, 2);
        }
    }

    @Override
    public void eveningCheck() {
        List<UserInfo> userList = userInfoMapper.selectAll();
        ApiInfo api = apiInfoMapper.selectOne(new ApiInfo(ApiConstant.TYPE_CHECK));

        for (UserInfo user : userList) {
            sendUtil.sendCheckRequest(user, api, 3);
        }
    }

    @Override
    public void singIn() {
        List<UserInfo> userList = userInfoMapper.selectAll();
        List<ApiInfo> apis = apiInfoMapper.select(new ApiInfo(ApiConstant.TYPE_SIGN_IN));
        ApiInfo signApi = null;
        ApiInfo getSignMessageApi = null;

        for (ApiInfo api : apis) {
            if (api.getName().equals(ApiConstant.GET_SIGN_MESSAGE))
                getSignMessageApi = api;
            else if (api.getName().equals(ApiConstant.DO_SIGN))
                signApi = api;
        }


        for (UserInfo user : userList) {
            SignMessage signMessage = sendUtil.getSignMessage(user, getSignMessageApi);
            sendUtil.sendSignRequest(user, signApi, signMessage);
        }
    }

}
