package com.atguigu.aclservice.controller.bus;


import com.atguigu.aclservice.entity.bus.BusForm;
import com.atguigu.aclservice.service.bus.IBusFormService;
import com.atguigu.aclservice.util.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 业务表单 前端控制器
 * </p>
 *
 * @author zwx
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/admin/acl/busform")
public class BusFormController extends BaseController<BusForm, IBusFormService> {

    public BusFormController() {
        super.PK = "id";
    }

}

