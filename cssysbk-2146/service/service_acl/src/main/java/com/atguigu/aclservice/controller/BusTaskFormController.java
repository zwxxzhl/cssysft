package com.atguigu.aclservice.controller;


import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.BusTaskForm;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.IBusTaskFormService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

    @ApiOperation(value = "更新任务表单")
    @PutMapping("update")
    public R update(@RequestBody BusTaskForm busTaskForm) {

        busTaskFormService.updateById(busTaskForm);
        return R.ok();
    }

    @ApiOperation(value = "保存任务表单")
    @PutMapping("save")
    public R save(@RequestBody JSONObject params) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        String formPid = params.getString("formPid");
        String procinstId = params.getString("procinstId");
        String procdefId = params.getString("procdefId");

        // 保存业务表单
        BusTaskForm busTaskForm = new BusTaskForm();
        busTaskForm.setPid(formPid);
        busTaskForm.setProcinstId(procinstId);
        busTaskForm.setProcdefId(procdefId);
        busTaskForm.setUserId(user.getId());
        busTaskForm.setTitle(params.getString("title"));
        busTaskForm.setContent(params.getString("content"));
        busTaskForm.setGmtCreateUser(user.getId());
        busTaskForm.setGmtUpdateUser(user.getId());

        busTaskFormService.save(busTaskForm);
        return R.ok();
    }

    @ApiOperation(value = "查询任务表单")
    @GetMapping("find")
    public R find(@RequestParam("id") String id) {
        BusTaskForm byId = busTaskFormService.getById(id);
        return R.ok().data(byId);
    }

    @ApiOperation(value = "查询任务树形表单")
    @GetMapping("selectTree")
    public R selectTree(@RequestParam("procinstId") String procinstId) {
        QueryWrapper<BusTaskForm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("procinst_id", procinstId);
        List<Map<String, Object>> list = busTaskFormService.listMaps(queryWrapper);

        Map<String, Object> firstMap = null;
        for (Map<String, Object> map : list) {
            List<Map<String, Object>> collect = list.stream()
                    .filter(f -> f.get("id").toString().equals(map.get("pid").toString()))
                    .collect(Collectors.toList());
            if (collect.size() == 0) {
                firstMap = map;
                break;
            }
        }

        List<Map<String, Object>> retList = new ArrayList<>();
        if (Optional.ofNullable(firstMap).isPresent()) {
            String userId = firstMap.get("userId").toString();

            List<Map<String, Object>> childList = new ArrayList<>();
            Map<String, List<Map<String, Object>>> map = list.stream().collect(Collectors.groupingBy(g -> g.get("userId").toString()));
            map.forEach((k, v) -> {
                if (!k.equals(userId)) {
                    Map<String, Object> childMap = new HashMap<>();
                    childMap.put("id", UUID.randomUUID().toString().replace("-", ""));
                    childMap.put("userId", k);
                    childMap.put("children", v);
                    childList.add(childMap);
                }
            });

            Map<String, Object> parentMap = new HashMap<>();
            parentMap.put("id", UUID.randomUUID().toString().replace("-", ""));
            parentMap.put("userId", firstMap.get("userId").toString());
            parentMap.put("title", firstMap.get("title").toString());
            parentMap.put("content", firstMap.get("content").toString());
            parentMap.put("children", childList);

            retList.add(parentMap);
        }

        return R.ok().data(R.ITEMS, retList);
    }

}

