package com.atguigu.aclservice.controller;


import com.atguigu.aclservice.entity.BusTaskForm;
import com.atguigu.aclservice.service.IBusTaskFormService;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 派发任务表单 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-05-31
 */
@RestController
@RequestMapping("/admin/acl/bustaskform")
public class BusTaskFormController {

    @Autowired
    IBusTaskFormService busTaskFormService;

    @ApiOperation(value = "更新任务表单")
    @PutMapping("update")
    public R update(@RequestBody BusTaskForm busTaskForm) {
        busTaskFormService.updateById(busTaskForm);
        return R.ok();
    }

}

