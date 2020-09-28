package uz.fuckwzxy.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uz.fuckwzxy.cons.ApiConstant;
import uz.fuckwzxy.entity.ApiInfo;
import uz.fuckwzxy.entity.SignMessage;
import uz.fuckwzxy.entity.UserInfo;


/**
 * @author wzh
 * 2020/9/23 19:15
 */
@Component
@Slf4j
public class SendUtil {

    @Async
    public void sendCheckRequest(UserInfo user, ApiInfo api, int seq) {
        HttpRequest request = createHttpRequest(api);
        //body
        request.body(api.getBody().replace("seq=3", "seq=" + seq));
        //token
        request.header("token", user.getToken());
        //user-agent
        request.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/53.0.2785.143 Safari/537.36" + " MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        //content-type
        request.contentType("application/x-www-form-urlencoded");
        //log
        log.info("{}同学调用了{}，完成了{}，接口地址是{}",
                user.getName(), api.getName(), api.getType().equals(ApiConstant.TYPE_CHECK) ? "三检" : "签到", api.getUrl());
        //return body
        request.execute();
    }

    /**
     * 获取签到的id和logId
     *
     * @param user              user
     * @param getSignMessageApi 接口
     */

    public SignMessage getSignMessage(UserInfo user, ApiInfo getSignMessageApi) {
        HttpRequest request = createHttpRequest(getSignMessageApi);
        //token
        request.header("token", user.getToken());
        //user-agent
        request.header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0.1; G0215D Build/MMB29M; wv) AppleWebKit/537.36" +
                " (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.62 XWEB/2690 MMWEBSDK/200502 Mobile Safari/537.36 MMWEBID/1918 " +
                "MicroMessenger/7.0.15.1680(0x27000F50) Process/appbrand1 WeChat/arm32 NetType/WIFI Language/zh_CN ABI/arm64");
        //content-type
        request.contentType("application/x-www-form-urlencoded");
        //参数
        request.form("page", 1);
        request.form("size", 5);
        //得到返回的JSON并解析
        String body = request.execute().body();
        //System.out.println(user.getName() + ":" + body);
        JSONObject data = (JSONObject) ((JSONArray) JSONUtil.parseObj(body).get("data")).get(0);
        return new SignMessage((String) data.getObj("id"), (String) data.get("logId"));
    }


    @Async
    public void sendSignRequest(UserInfo user, ApiInfo signApi, SignMessage signMessage) {
        HttpRequest request = createHttpRequest(signApi);
        //JSON data
        JSONObject data = new JSONObject();
        data.set("id", signMessage.getLogId());
        data.set("signId", signMessage.getId());
        data.set("latitude", "23.090164");
        data.set("longitude", "113.354053");
        data.set("country", "中国");
        data.set("province", "广东省");
        data.set("district", "海珠区");
        data.set("township", "官洲街道");
        data.set("city", "广州市");
        request.body(data.toString());
        //token
        request.header("token", user.getToken());
        //cookie
        //request.cookie(new HttpCookie("SESSION", user.getSession()), new HttpCookie("path", "/"));
        //content-type
        request.contentType("application/json");
        request.execute();
        log.info("{}同学调用了{}，完成了{}，接口地址是{}",
                user.getName(), signApi.getName(), signApi.getType().equals(ApiConstant.TYPE_CHECK) ? "三检" : "签到", signApi.getUrl());
    }

    /**
     * 创建HttpRequest对象
     */
    private HttpRequest createHttpRequest(ApiInfo api) {
        HttpRequest request = null;
        //get
        if (api.getMethod().equals(ApiConstant.METHOD_GET))
            request = HttpRequest.get(api.getUrl());
            //post
        else if (api.getMethod().equals(ApiConstant.METHOD_POST))
            request = HttpRequest.post(api.getUrl());
        return request;
    }


}
