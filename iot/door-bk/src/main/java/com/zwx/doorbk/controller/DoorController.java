package com.zwx.doorbk.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.PubRequest;
import com.aliyuncs.iot.model.v20180120.PubResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zwx.doorbk.utils.Result;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/door")
public class DoorController {

    /**
     * 打开门
     * @param scope 数值
     * {"id":"123","version":"1.0","method":"thing.event.property.set","params":{"pro_01_switch":2}}
     */
    @GetMapping("/open")
    public Result open(int scope) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", "123");
        obj.addProperty("version", "1.0");
        obj.addProperty("method", "thing.service.property.set");

        JsonObject params = new JsonObject();
        params.addProperty("pro_01_switch", scope);
        obj.add("params", params);

        byte[] bytes = obj.toString().getBytes(StandardCharsets.UTF_8);
        String base64 = Base64Utils.encodeToString(bytes);

        sentSet(base64);
        return Result.ok();
    }

    private void sentSet(String msg) {
        String accessKey = ""; //您阿里云账号的AccessKey。
        String accessKeySecret = ""; //您阿里云账号的AccessKey Secret。
        String iotInstanceId = ""; //您企业版实例的实例ID。
        String productKey = ""; //产品的ProductKey。

        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKey, accessKeySecret); //cn-shenzhen为实例所属地域，需修改为您实例的所属地域。
        DefaultAcsClient client = new DefaultAcsClient(profile);

        PubRequest request = new PubRequest();
        request.setSysEndpoint("iot.cn-shanghai.aliyuncs.com"); //您实例所属地域的API服务端地址。
        request.setTopicFullName("/sys/a19NUVLGQcS/MQTT_01/thing/service/property/set"); //发布消息的Topic。
        request.setMessageContent(msg);

        request.setProductKey(productKey);
        request.setIotInstanceId(iotInstanceId);
        request.setQos(1);
        try {
            PubResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }


}
