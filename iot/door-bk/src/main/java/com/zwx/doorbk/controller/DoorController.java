package com.zwx.doorbk.controller;

import com.zwx.doorbk.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/door")
public class DoorController {

    private String url = "https://a19NUVLGQcS.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";

    @GetMapping("/open")
    public Result open() {



        return Result.ok().data("open", "ok");
    }

}
