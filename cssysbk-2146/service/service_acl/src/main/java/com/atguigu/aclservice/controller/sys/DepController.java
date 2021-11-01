package com.atguigu.aclservice.controller.sys;

import com.atguigu.aclservice.entity.sys.Dep;
import com.atguigu.aclservice.service.sys.IDepService;
import com.atguigu.aclservice.util.BaseController;
import com.atguigu.aclservice.util.JacksonCusUtil;
import com.atguigu.utils.utils.Helpers;
import com.atguigu.utils.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class DepController extends BaseController<Dep, IDepService> {

    public DepController() {
        super.PK = "id";
        super.UNI_VALID = true;
        super.UNI_PROP = "depNo";
        super.WRAPPER_ADD_DELETE = false;
        super.DELETE_PROP = "isDeleted";
    }

    @ApiOperation(value = "获取部门(树形数据)")
    @GetMapping("/selectTree")
    public R selectTree(@ApiParam(name = "entity", value = "数据对象") Dep dep) {
        List<Dep> list = entityService.list(getQueryWrapper(dep));
        List<Map<String, Object>> allList = JacksonCusUtil.toListMap(list);

        List<Map<String, Object>> treeList = allList.stream().filter(f -> Objects.equals(f.get("pid"), "0"))
                .peek(m -> {
                    List<Map<String, Object>> children = getChildren(m, allList);
                    m.put("children", children);
                    m.put("leaf", children.size() == 0);
                }).collect(Collectors.toList());

        return R.ok().data(R.ITEMS, treeList);
    }

    private List<Map<String, Object>> getChildren(Map<String, Object> m, List<Map<String, Object>> allList) {
        return allList.stream().filter(f -> Objects.equals(m.get("id"), f.get("pid")))
                .peek(p -> {
                    List<Map<String, Object>> children = getChildren(p, allList);
                    p.put("children", children);
                    p.put("leaf", children.size() == 0);
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

