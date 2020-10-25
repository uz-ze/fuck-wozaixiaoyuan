package uz.fuckwzxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
 * 2020/9/23 19:00
 */
@RestController
@Slf4j
public class A {
    @RequestMapping("/ss")
    public void test() {
        log.info("sdsaffsdf");
        int i = 1 / 0;
    }

    @Autowired
    CheckService checkService;
    @RequestMapping("/go")
    public void go() {
        checkService.morningCheck();
    }

    @RequestMapping("/gogo")
    public void gogo() {
        checkService.singIn();
    }
    @Autowired
    SendUtil sendUtil;
    @Resource
    UserInfoMapper mapper;
    @Resource
    ApiInfoMapper apiInfoMapper;

    @RequestMapping("/gogogogo")
    public void gogogo() {
        List<UserInfo> userList = mapper.selectAll();
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
            //sendUtil.sendSignRequest(user, signApi, signMessage);
            System.out.println(signMessage);
        }
    }
}
