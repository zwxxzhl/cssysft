package com.atguigu.aclservice.util;

import com.atguigu.aclservice.service.sys.UserService;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 通用Controller
 */
public abstract class BaseController<E, S extends IService<E>> {

    protected String PK;
    protected boolean UNI_VALID = false;
    protected String UNI_PROP;
    // 保存与更新唯一校验时，是否加上删除条件
    protected boolean WRAPPER_ADD_DELETE = false;
    protected String DELETE_PROP;

    protected final S entityService;
    protected final UserService userService;

    public BaseController(S entityService, UserService userService) {
        this.entityService = entityService;
        this.userService = userService;
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public R save(@ApiParam(name = "entity", value = "数据对象") @RequestBody E entity) {
        try {
            if (UNI_VALID) {
                QueryWrapper<E> wrapper = new QueryWrapper<>();
                String code = (String) ReflectCusUtil.getValue(entity, UNI_PROP);
                if (!StringUtils.hasText(code)) {
                    wrapper.eq(AuxProUtil.toUnderlineCase(UNI_PROP), code);
                }
                if (WRAPPER_ADD_DELETE) {
                    wrapper.eq(AuxProUtil.toUnderlineCase(DELETE_PROP), false);
                }
                wrapper.last("limit 1");
                List<E> list = entityService.list(wrapper);
                if (list.size() > 0) {
                    return R.ok().success(false).message("唯一校验失败");
                }
            }
            AuxProUtil.bindEntityCreate(entity, AuxProUtil.getLoginUser(userService));
            entityService.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增出错");
        }

        return R.ok();
    }

    @ApiOperation(value = "更新")
    @PutMapping("update")
    public R update(@ApiParam(name = "entity", value = "数据对象") @RequestBody E entity) {
        try {
            if (UNI_VALID) {
                QueryWrapper<E> wrapper = new QueryWrapper<>();
                String code = (String) ReflectCusUtil.getValue(entity, UNI_PROP);
                String id = (String) ReflectCusUtil.getValue(entity, PK);
                if (!StringUtils.hasText(code)) {
                    wrapper.eq(AuxProUtil.toUnderlineCase(UNI_PROP), code);
                }
                if (WRAPPER_ADD_DELETE) {
                    wrapper.eq(AuxProUtil.toUnderlineCase(DELETE_PROP), false);
                }
                wrapper.last("limit 2");
                List<E> list = entityService.list(wrapper);

                if (list.size() >= 2 || (list.size() == 1 && !ReflectCusUtil.getValue(list.get(0), PK).equals(id))) {
                    return R.ok().success(false).message("唯一校验失败");
                }
            }
            AuxProUtil.bindEntityUpdate(entity, AuxProUtil.getLoginUser(userService));
            entityService.updateById(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新出错");
        }
        return R.ok();
    }

    @ApiOperation(value = "真实删除")
    @DeleteMapping("remove")
    public R remove(@ApiParam(name = "ids", value = "id数组") @RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            entityService.removeByIds(ids);
        } else {
            return R.error().message("未传入删除参数");
        }
        return R.ok();
    }

    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("remove/logic")
    public R removeLogic(@ApiParam(name = "ids", value = "id数组") @RequestBody List<String> ids) {
        if (Optional.ofNullable(ids).isPresent() && ids.size() > 0) {
            Collection<E> dicts = entityService.listByIds(ids);
            for (E item : dicts) {
                try {
                    ReflectCusUtil.setValue(item, DELETE_PROP, true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("删除出错");
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
            @ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,
            @ApiParam(name = "params", value = "查询对象") @RequestParam Map<String, Object> params) {

        IPage<E> pageModel = entityService.page(new Page<>(page, limit), AuxProUtil.jsonParamsWrapper(new QueryWrapper<>(), params));
        return R.ok().data(R.ITEMS, pageModel.getRecords()).data(R.TOTAL, pageModel.getTotal());
    }

    @ApiOperation(value = "列表(不分页)")
    @GetMapping("/select")
    public R select(@ApiParam(name = "params", value = "查询对象") @RequestParam Map<String, Object> params) {
        List<E> list = entityService.list(AuxProUtil.jsonParamsWrapper(new QueryWrapper<>(), params));
        return R.ok().data(R.ITEMS, list);
    }

}
