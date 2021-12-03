package com.atguigu.aclservice.controller.bus;


import com.atguigu.aclservice.entity.bus.BusForm;
import com.atguigu.aclservice.service.bus.IBusFormService;
import com.atguigu.aclservice.service.sys.UserService;
import com.atguigu.aclservice.util.BaseController;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    public BusFormController(IBusFormService entityService, UserService userService) {
        super(entityService, userService);
        super.PK = "id";
    }

    @ApiOperation(value = "保存业务表单")
    @PostMapping("saveCus")
    public R save(@ApiParam(name = "entity", value = "数据对象") @RequestBody Map<String, Object> params) {
        entityService.saveCus(params);
        return R.ok();
    }

}

