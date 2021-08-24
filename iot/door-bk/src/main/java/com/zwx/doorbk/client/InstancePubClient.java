package com.zwx.doorbk.client;

import com.aliyuncs.DefaultAcsClient;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.PubRequest;
import com.aliyuncs.iot.model.v20180120.PubResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;

public class InstancePubClient {

    private static String accessKey = ""; //您阿里云账号的AccessKey。
    private static String accessKeySecret = ""; //您阿里云账号的AccessKey Secret。
    private static String iotInstanceId = ""; //您企业版实例的实例ID。
    public static void main(String[] args)  {

        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKey, accessKeySecret); //cn-shenzhen为实例所属地域，需修改为您实例的所属地域。
        DefaultAcsClient client = new DefaultAcsClient(profile);

        PubRequest request = new PubRequest();
        request.setSysEndpoint("iot.cn-shanghai.aliyuncs.com"); //您实例所属地域的API服务端地址。
        request.setTopicFullName("/sys/a19NUVLGQcS/MQTT_01/thing/service/property/set"); //发布消息的Topic。
        //request.setMessageContent("Q01ELDgyOTIzLGFkMzIyCiA=");//原始报文 : CMD,82923,ad322。

        // 开 eyJpZCI6IjQ0NjA4OTI3OCIsInZlcnNpb24iOiIxLjAiLCJtZXRob2QiOiJ0aGluZy5zZXJ2aWNlLnByb3BlcnR5LnNldCIsInBhcmFtcyI6eyJwcm9fMDFfc3dpdGNoIjoxfX0=
        // 关 eyJpZCI6IjQ0NjA4OTI3OCIsInZlcnNpb24iOiIxLjAiLCJtZXRob2QiOiJ0aGluZy5zZXJ2aWNlLnByb3BlcnR5LnNldCIsInBhcmFtcyI6eyJwcm9fMDFfc3dpdGNoIjowfX0=
        request.setMessageContent("eyJpZCI6IjQ0NjA4OTI3OCIsInZlcnNpb24iOiIxLjAiLCJtZXRob2QiOiJ0aGluZy5zZXJ2aWNlLnByb3BlcnR5LnNldCIsInBhcmFtcyI6eyJwcm9fMDFfc3dpdGNoIjoxfX0=");
        request.setProductKey("a19NUVLGQcS"); //产品的ProductKey。
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