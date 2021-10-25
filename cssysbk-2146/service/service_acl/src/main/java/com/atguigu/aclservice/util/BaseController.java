package com.atguigu.aclservice.util;

import com.atguigu.utils.utils.Helpers;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseController<E, ES extends IService<E>, U, US extends IService<U>> {

    public ImmutablePair<ES, US> initService(ES entityService, US userService) {
        return ImmutablePair.of(entityService, userService);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public R save(@RequestBody E entity) {
        try {
            // initService(entityService, userService);

            QueryWrapper<E> wrapper = new QueryWrapper<>();
            String code = (String) AuxProUtil.getValue(entity, "code");
            if (Helpers.isNotBlank(code)) {
                wrapper.eq("code", code);
            }
            List<E> list = entityService.list(wrapper);
            if (list.size() > 0) {
                return R.ok().success(false).message("编码已存在");
            }

            AuxProUtil.bindEntityCreate(entity, AuxProUtil.getLoginUser(userService));
            entityService.save(entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("反射出错");
        }

        return R.ok();
    }

    @ApiOperation(value = "更新")
    @PutMapping("update")
    public R update(@RequestBody E entity) {
        try {
            QueryWrapper<E> wrapper = new QueryWrapper<>();
            String code = (String) AuxProUtil.getValue(entity, "code");
            String id = (String) AuxProUtil.getValue(entity, "id");
            if (Helpers.isNotBlank(code)) {
                wrapper.eq("code", code);
            }
            List<E> list = entityService.list(wrapper);

            String idQ = (String) AuxProUtil.getValue(list.get(0), "id");
            if (list.size() > 2 || (list.size() == 1 && !idQ.equals(id))) {
                return R.ok().success(false).message("编码已存在");
            }

            AuxProUtil.bindEntityUpdate(entity, AuxProUtil.getLoginUser(userService));
            entityService.updateById(entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("反射出错");
        }
        return R.ok();
    }

    @ApiOperation(value = "真实删除字典")
    @DeleteMapping("remove")
    public R remove(@RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            entityService.removeByIds(ids);
        } else {
            return R.error().message("未传入删除参数");
        }
        return R.ok();
    }

    @ApiOperation(value = "逻辑删除字典")
    @DeleteMapping("remove/logic")
    public R removeLogic(@RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            Collection<E> dicts = entityService.listByIds(ids);
            for (E item : dicts) {
                try {
                    AuxProUtil.setValue(item, "isDeleted", true);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("反射出错");
                }
            }

            entityService.updateBatchById(dicts);
        } else {
            return R.error().message("未传入删除参数");
        }
        return R.ok();
    }

    @ApiOperation(value = "列表(分页)")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象")
                    E entity) {

        Page<E> pageParam = new Page<>(page, limit);
        IPage<E> pageModel = entityService.page(pageParam, AuxProUtil.initQueryWrapper(entity, Collections.emptyList(), (wrapper) -> {}));

        return R.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
    }

    @ApiOperation(value = "列表(不分页)")
    @GetMapping("/select")
    public R select(E entity) {
        List<E> list = entityService.list(AuxProUtil.initQueryWrapper(entity, Collections.emptyList(), (wrapper) -> {}));
        return R.ok().data(R.ITEMS, list);
    }

}
