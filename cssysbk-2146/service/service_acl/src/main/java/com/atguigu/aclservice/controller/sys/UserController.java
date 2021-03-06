package com.atguigu.aclservice.controller.sys;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.aclservice.entity.sys.User;
import com.atguigu.aclservice.service.sys.RoleService;
import com.atguigu.aclservice.service.sys.UserService;
import com.atguigu.utils.utils.MD5;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/user")
//@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
             User userQueryVo) {
        Page<User> pageParam = new Page<>(page, limit);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(userQueryVo.getUsername())) {
            wrapper.like("username",userQueryVo.getUsername());
        }

        IPage<User> pageModel = userService.page(pageParam, wrapper);
        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("save")
    public R save(@RequestBody User user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        userService.save(user);
        return R.ok();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("update")
    public R update(@RequestBody User user) {
        user.setPassword(null);
        userService.updateById(user);
        return R.ok();
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("update/pwd")
    public R updatePwd(@RequestBody Map<String, Object> params) {
        User user = userService.getById(params.get("id").toString());
        if (Optional.ofNullable(user).isPresent()) {
            if (user.getPassword().equals(MD5.encrypt(params.get("password").toString()))) {
                user.setPassword(MD5.encrypt(params.get("pwdOne").toString()));
                userService.updateById(user);
            } else {
                return R.ok().success(false).message("旧密码输入有误");
            }
        } else {
            return R.ok().success(false).message("用户不存在");
        }
        return R.ok();
    }

    @ApiOperation(value = "校验用户旧密码")
    @PutMapping("update/pwd/valid")
    public R updatePwdValid(@RequestBody Map<String, Object> params) {
        User user = userService.getById(params.get("id").toString());
        if (Optional.ofNullable(user).isPresent()) {
            if (!user.getPassword().equals(MD5.encrypt(params.get("password").toString()))) {
                return R.ok().success(false).message("旧密码输入有误");
            }
        } else {
            return R.ok().success(false).message("用户不存在");
        }
        return R.ok();
    }

    @ApiOperation(value = "根据Id获取用户数据")
    @GetMapping("/get/{userId}")
    public R get(@PathVariable String userId) {
        User user = userService.getById(userId);
        return R.ok().data(JSON.parseObject(JSON.toJSONString(user),
                new TypeReference<Map<String,Object>>(){}));
    }

    @ApiOperation(value = "获取所有用户")
    @GetMapping("/select")
    public R select() {
        List<User> list = userService.list(new QueryWrapper<>());
        return R.ok().data(R.ITEMS, list);
    }

    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public R toAssign(@PathVariable String userId) {
        Map<String, Object> roleMap = roleService.findRoleByUserId(userId);
        return R.ok().data(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public R doAssign(@RequestParam String userId,@RequestParam String[] roleId) {
        roleService.saveUserRoleRealtionShip(userId,roleId);
        return R.ok();
    }
}

