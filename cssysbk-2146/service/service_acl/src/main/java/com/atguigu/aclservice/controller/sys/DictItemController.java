package com.atguigu.aclservice.controller.sys;


import com.atguigu.aclservice.entity.sys.DictItem;
import com.atguigu.aclservice.entity.sys.User;
import com.atguigu.aclservice.service.sys.IDictItemService;
import com.atguigu.aclservice.service.sys.UserService;
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
 * 字典明细表 前端控制器
 * </p>
 *
 * @author zwx
 * @since 2021-10-25
 */
@RestController
@RequestMapping("/admin/acl/dict/item")
public class DictItemController {

    @Autowired
    IDictItemService dictItemService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "新增字典明细")
    @PostMapping("save")
    public R save(@RequestBody DictItem entity) {
        QueryWrapper<DictItem> wrapper = new QueryWrapper<>();
        if (Helpers.isNotBlank(entity.getCode())) {
            wrapper.eq("pid", entity.getPid());
            wrapper.eq("code", entity.getCode());
        }
        List<DictItem> list = dictItemService.list(wrapper);
        if (list.size() > 0) {
            return R.ok().success(false).message("字典明细编码已存在");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        entity.setGmtCreateUser(user.getId());
        entity.setGmtUpdateUser(user.getId());

        dictItemService.save(entity);
        return R.ok();
    }

    @ApiOperation(value = "更新字典明细")
    @PutMapping("update")
    public R update(@RequestBody DictItem entity) {
        QueryWrapper<DictItem> wrapper = new QueryWrapper<>();
        if (Helpers.isNotBlank(entity.getCode())) {
            wrapper.eq("pid", entity.getPid());
            wrapper.eq("code", entity.getCode());
        }
        List<DictItem> list = dictItemService.list(wrapper);
        if (list.size() > 2 || (list.size() == 1 && !list.get(0).getId().equals(entity.getId()))) {
            return R.ok().success(false).message("字典明细编码已存在");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.selectByUsername(username);

        entity.setGmtUpdateUser(user.getId());

        dictItemService.updateById(entity);
        return R.ok();
    }

    @ApiOperation(value = "真实删除字典明细")
    @DeleteMapping("remove")
    public R remove(@RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            dictItemService.removeByIds(ids);
        } else {
            return R.error().message("未传入删除参数");
        }
        return R.ok();
    }

    @ApiOperation(value = "逻辑删除字典明细")
    @DeleteMapping("remove/logic")
    public R removeLogic(@RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            Collection<DictItem> dictItems = dictItemService.listByIds(ids);
            dictItems.forEach(e -> e.setIsDeleted(true));

            dictItemService.updateBatchById(dictItems);
        } else {
            return R.error().message("未传入删除参数");
        }
        return R.ok();
    }

    @ApiOperation(value = "获取字典明细(分页)")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    DictItem dict) {

        Page<DictItem> pageParam = new Page<>(page, limit);
        IPage<DictItem> pageModel = dictItemService.page(pageParam, getQueryWrapper(dict));

        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "获取字典明细(不分页)")
    @GetMapping("/select")
    public R select(DictItem dict) {
        List<DictItem> list = dictItemService.list(getQueryWrapper(dict));
        return R.ok().data(R.ITEMS, list);
    }

    private QueryWrapper<DictItem> getQueryWrapper(DictItem entity) {
        QueryWrapper<DictItem> wrapper = new QueryWrapper<>();
        if (Helpers.isNotBlank(entity.getId())) {
            wrapper.eq("id", entity.getId());
        }
        if (Helpers.isNotBlank(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        if (Helpers.isNotBlank(entity.getCode())) {
            wrapper.eq("code", entity.getCode());
        }
        return wrapper;
    }

}

