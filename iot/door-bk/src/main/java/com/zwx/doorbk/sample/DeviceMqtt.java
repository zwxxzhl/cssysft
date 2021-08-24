package com.zwx.doorbk.sample;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.Gson;
import com.zwx.doorbk.controller.DoorController;
import com.zwx.doorbk.utils.FileUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DeviceMqtt {

    final static String TAG = "MqttSample";
    private String pk, dn;

    public static void main(String[] args) {
        ALog.d(TAG, "IOT START!");
        ALog.setLevel(ALog.LEVEL_DEBUG);
        ALog.d(TAG, "args=" + Arrays.toString(args));
        System.out.println(System.getProperty("user.dir"));

        String diPath = System.getProperty("user.dir") + "/device_id.json";
        String deviceInfo = FileUtils.readFile(diPath);
        if (deviceInfo == null) {
            ALog.e(TAG, "main - need device info path.");
            return;
        }

        Gson mGson = new Gson();
        DeviceInfoData deviceInfoData = mGson.fromJson(deviceInfo, DeviceInfoData.class);
        if (deviceInfoData == null) {
            ALog.e(TAG, "main - deviceInfo format error.");
            return;
        }

        DeviceMqtt manager = new DeviceMqtt();
        manager.init(deviceInfoData);
    }

    public void init(final DeviceInfoData deviceInfoData) {
        this.pk = deviceInfoData.productKey;
        this.dn = deviceInfoData.deviceName;
        LinkKitInitParams params = new LinkKitInitParams();
        /**
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = deviceInfoData.productKey;
        config.deviceName = deviceInfoData.deviceName;
        config.deviceSecret = deviceInfoData.deviceSecret;
        config.channelHost = pk + ".iot-as-mqtt." + deviceInfoData.region + ".aliyuncs.com:1883";
        /**
         * 是否接受离线消息
         * 对应 mqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;
        params.mqttClientConfig = config;

        /**
         * 设置初始化三元组信息，用户传入
         */
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = pk;
        deviceInfo.deviceName = dn;
        deviceInfo.deviceSecret = deviceInfoData.deviceSecret;

        params.deviceInfo = deviceInfo;

        /**
         * 设置设备当前的初始状态值，属性需要和云端创建的物模型属性一致
         * 如果这里什么属性都不填，物模型就没有当前设备相关属性的初始值。
         * 用户调用物模型上报接口之后，物模型会有相关数据缓存。
         */
        Map<String, ValueWrapper> propertyValues = new HashMap<String, ValueWrapper>();
        // 示例
        // propertyValues.put("LightSwitch", new ValueWrapper.BooleanValueWrapper(0));
        params.propertyValues = propertyValues;
        params.fmVersion = "1.0.2";

        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            public void onError(AError aError) {
                ALog.e(TAG, "Init Error error=" + aError);
            }

            public void onInitDone(InitResult initResult) {
                ALog.i(TAG, "onInitDone result=" + initResult);
                executeScheduler(deviceInfoData);
            }
        });
    }

    /**
     * 定时执行
     */
    public void executeScheduler(DeviceInfoData deviceInfoData) {
        System.out.println("进入=======");
        testMqtt();
        testCota();
    }

    /**
     * 测试 Mqtt 基础topic封装
     * 发布
     * 订阅
     * 取消订阅
     * 注册资源监听，一般用于服务
     */
    private void testMqtt() {
        MqttSample sample = new MqttSample(pk, dn);
        sample.publish();
        sample.subscribe();
//        sample.unSubscribe();
//        sample.registerResource();
    }

    /**
     * 测试 COTA 远程配置
     */
    private void testCota() {
        COTASample sample = new COTASample(pk, dn);
        // 监听云端 COTA 下行数据更新
        sample.setCOTAChangeListener();
        // 获取 COTA 更新
//        sample.cOTAGet();
    }


}
