package com.atguigu.aclservice.controller;


import com.atguigu.aclservice.entity.Dep;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.IDepService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-08-16
 */
@RestController
@RequestMapping("/admin/acl/dep")
public class DepController {

    @Autowired
    IDepService depService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "新增部门")
    @PostMapping("save")
    public R save(@RequestBody Dep dep) {
        QueryWrapper<Dep> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(dep.getDepNo())) {
            wrapper.eq("dep_no", dep.getDepNo());
        }
        List<Dep> list = depService.list(wrapper);
        if (list.size() > 0) {
            return R.error().message("部门编码已存在!");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        dep.setGmtCreateUser(user.getId());
        dep.setGmtUpdateUser(user.getId());

        depService.save(dep);
        return R.ok();
    }

    @ApiOperation(value = "更新部门")
    @PutMapping("update")
    public R update(@RequestBody Dep dep) {
        QueryWrapper<Dep> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(dep.getDepNo())) {
            wrapper.eq("dep_no", dep.getDepNo());
        }
        List<Dep> list = depService.list(wrapper);
        if (list.size() > 2 || (list.size() == 1 && !list.get(0).getId().equals(dep.getId()))) {
            return R.error().message("部门编码已存在!");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        dep.setGmtUpdateUser(user.getId());

        depService.updateById(dep);
        return R.ok();
    }

    @ApiOperation(value = "获取部门(分页)")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    Dep queryVo) {
        Page<Dep> pageParam = new Page<>(page, limit);
        QueryWrapper<Dep> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(queryVo.getId())) {
            wrapper.eq("id", queryVo.getId());
        }
        if(!StringUtils.isEmpty(queryVo.getDepName())) {
            wrapper.like("dep_name", queryVo.getDepName());
        }
        if(!StringUtils.isEmpty(queryVo.getDepNo())) {
            wrapper.eq("dep_no", queryVo.getDepNo());
        }

        IPage<Dep> pageModel = depService.page(pageParam, wrapper);
        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "获取部门(不分页)")
    @GetMapping("/select")
    public R select(Dep queryVo) {
        QueryWrapper<Dep> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(queryVo.getId())) {
            wrapper.eq("id", queryVo.getId());
        }
        if(!StringUtils.isEmpty(queryVo.getDepName())) {
            wrapper.like("dep_name", queryVo.getDepName());
        }
        if(!StringUtils.isEmpty(queryVo.getDepNo())) {
            wrapper.eq("dep_no", queryVo.getDepNo());
        }

        List<Dep> list = depService.list(wrapper);
        return R.ok().data(R.ITEMS, list);
    }

}

