package com.atguigu.aclservice.controller;

import com.atguigu.aclservice.entity.Dep;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.IDepService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.aclservice.util.AuxProUtil;
import com.atguigu.utils.utils.Helpers;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (Helpers.isNotBlank(dep.getDepNo())) {
            wrapper.eq("dep_no", dep.getDepNo());
        }
        List<Dep> list = depService.list(wrapper);
        if (list.size() > 0) {
            return R.ok().success(false).message("部门编码已存在");
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
        if (Helpers.isNotBlank(dep.getDepNo())) {
            wrapper.eq("dep_no", dep.getDepNo());
        }
        List<Dep> list = depService.list(wrapper);
        if (list.size() > 2 || (list.size() == 1 && !list.get(0).getId().equals(dep.getId()))) {
            return R.ok().success(false).message("部门编码已存在");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        dep.setGmtUpdateUser(user.getId());

        depService.updateById(dep);
        return R.ok();
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("remove")
    public R remove(@RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            depService.removeByIds(ids);
        } else {
            return R.error().message("未传入删除参数");
        }
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
                    Dep dep) {

        Page<Dep> pageParam = new Page<>(page, limit);
        IPage<Dep> pageModel = depService.page(pageParam, getQueryWrapper(dep));

        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "获取部门(不分页)")
    @GetMapping("/select")
    public R select(Dep dep) {
        List<Dep> list = depService.list(getQueryWrapper(dep));
        return R.ok().data(R.ITEMS, list);
    }

    @ApiOperation(value = "获取部门(树形数据)")
    @GetMapping("/selectTree")
    public R selectTree(Dep dep) {
        List<Dep> list = depService.list(getQueryWrapper(dep));

        Type type = new TypeToken<List<Map<String, Object>>>() {}.getType();
        Gson gson = AuxProUtil.gsonBuilder(type);
        List<Map<String, Object>> allList = gson.fromJson(gson.toJson(list), type);

        List<Map<String, Object>> treeList = allList.stream().filter(f -> Objects.equals(f.get("pid"), "0"))
                .peek(m -> {
                    List<Map<String, Object>> children = getChildren(m, allList);
                    m.put("children", children);
                    m.put("leaf", children.size() > 0);
                }).collect(Collectors.toList());

        return R.ok().data(R.ITEMS, treeList);
    }

    private List<Map<String, Object>> getChildren(Map<String, Object> m, List<Map<String, Object>> allList) {
        return allList.stream().filter(f -> Objects.equals(m.get("id"), f.get("pid")))
                .peek(p -> {
                    List<Map<String, Object>> children = getChildren(p, allList);
                    p.put("children", children);
                    p.put("leaf", children.size() > 0);
                }).collect(Collectors.toList());
    }

    private QueryWrapper<Dep> getQueryWrapper(Dep dep) {
        QueryWrapper<Dep> wrapper = new QueryWrapper<>();
        if (Helpers.isNotBlank(dep.getId())) {
            wrapper.eq("id", dep.getId());
        }
        if (Helpers.isNotBlank(dep.getDepName())) {
            wrapper.like("dep_name", dep.getDepName());
        }
        if (Helpers.isNotBlank(dep.getDepNo())) {
            wrapper.eq("dep_no", dep.getDepNo());
        }
        return wrapper;
    }

}

