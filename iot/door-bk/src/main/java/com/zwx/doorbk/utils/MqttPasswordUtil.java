package com.zwx.doorbk.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MqttPasswordUtil {

    public static String sign(Map<String, String> params, String deviceSecret, String signMethod) {
        //将参数Key按字典顺序排序
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        //生成规范化请求字符串
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            if ("sign".equalsIgnoreCase(key)) {
                continue;
            }
            canonicalizedQueryString.append(key).append(params.get(key));
        }

        try {
            String key = deviceSecret;
            return encryptHMAC(signMethod, canonicalizedQueryString.toString(), key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptHMAC(String signMethod, String content, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes("utf-8"), signMethod);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] data = mac.doFinal(content.getBytes("utf-8"));
        return bytesToHexString(data);
    }

    public static final String bytesToHexString(byte[] bArray) {

        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String args[]) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("productKey", "a19NUVLGQcS");
        params.put("deviceName", "MQTT_01");
        params.put("clientId", "MQTT_01");
        //时间戳，可选参数。传入时，删除下面两行代码前的正斜线
        //String t = System.currentTimeMillis() + "";
        //params.put("timestamp", t);

        String sign = MqttPasswordUtil.sign(params, "d581731eda9e40745d89f47fce58b3f5", "hmacsha1");
        System.out.print(sign);
        System.out.println(System.getProperty("user.dir"));
    }

}
