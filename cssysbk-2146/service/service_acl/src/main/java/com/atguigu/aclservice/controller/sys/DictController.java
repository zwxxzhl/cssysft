package com.atguigu.aclservice.controller.sys;


import com.atguigu.aclservice.entity.sys.Dict;
import com.atguigu.aclservice.entity.sys.User;
import com.atguigu.aclservice.service.sys.IDictService;
import com.atguigu.aclservice.service.sys.UserService;
import com.atguigu.aclservice.util.BaseController;
import com.atguigu.utils.utils.Helpers;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 字典类别表 前端控制器
 * </p>
 *
 * @author zwx
 * @since 2021-10-25
 */
@RestController
@RequestMapping("/admin/acl/dict")
public class DictController extends BaseController<Dict, IDictService> {

    public DictController() {
        super.uniValid = true;
        super.uniProp = "code";
    }

    /*@ApiOperation(value = "新增字典")
    @PostMapping("save")
    public R save(@RequestBody Dict dict) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        if (Helpers.isNotBlank(dict.getCode())) {
            wrapper.eq("code", dict.getCode());
        }
        List<Dict> list = dictService.list(wrapper);
        if (list.size() > 0) {
            return R.ok().success(false).message("字典编码已存在");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        dict.setGmtCreateUser(user.getId());
        dict.setGmtUpdateUser(user.getId());

        dictService.save(dict);
        return R.ok();
    }

    @ApiOperation(value = "更新字典")
    @PutMapping("update")
    public R update(@RequestBody Dict dict) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        if (Helpers.isNotBlank(dict.getCode())) {
            wrapper.eq("code", dict.getCode());
        }
        List<Dict> list = dictService.list(wrapper);
        if (list.size() > 2 || (list.size() == 1 && !list.get(0).getId().equals(dict.getId()))) {
            return R.ok().success(false).message("字典编码已存在");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        dict.setGmtUpdateUser(user.getId());

        dictService.updateById(dict);
        return R.ok();
    }

    @ApiOperation(value = "真实删除字典")
    @DeleteMapping("remove")
    public R remove(@RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            dictService.removeByIds(ids);
        } else {
            return R.error().message("未传入删除参数");
        }
        return R.ok();
    }

    @ApiOperation(value = "逻辑删除字典")
    @DeleteMapping("remove/logic")
    public R removeLogic(@RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            Collection<Dict> dicts = dictService.listByIds(ids);
            dicts.forEach(e -> e.setIsDeleted(true));

            dictService.updateBatchById(dicts);
        } else {
            return R.error().message("未传入删除参数");
        }
        return R.ok();
    }

    @ApiOperation(value = "获取字典(分页)")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    Dict dict) {

        Page<Dict> pageParam = new Page<>(page, limit);
        IPage<Dict> pageModel = dictService.page(pageParam, getQueryWrapper(dict));

        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "获取字典(不分页)")
    @GetMapping("/select")
    public R select(Dict dict) {
        List<Dict> list = dictService.list(getQueryWrapper(dict));
        return R.ok().data(R.ITEMS, list);
    }

    private QueryWrapper<Dict> getQueryWrapper(Dict dict) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        if (Helpers.isNotBlank(dict.getId())) {
            wrapper.eq("id", dict.getId());
        }
        if (Helpers.isNotBlank(dict.getName())) {
            wrapper.like("name", dict.getName());
        }
        if (Helpers.isNotBlank(dict.getCode())) {
            wrapper.eq("code", dict.getCode());
        }
        return wrapper;
    }*/
}

