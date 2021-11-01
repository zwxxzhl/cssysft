package com.atguigu.aclservice.controller.sys;


import com.atguigu.aclservice.entity.sys.DictItem;
import com.atguigu.aclservice.service.sys.IDictItemService;
import com.atguigu.aclservice.util.BaseController;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class DictItemController extends BaseController<DictItem, IDictItemService> {

    public DictItemController() {
        super.PK = "id";
        super.UNI_VALID = true;
        super.UNI_PROP = "code";
        super.WRAPPER_ADD_DELETE = false;
        super.DELETE_PROP = "isDeleted";
    }

    @ApiOperation(value = "批量获取字典数据")
    @GetMapping("/list/batch")
    public R select(@ApiParam(name = "codes", value = "字典编码数组", required = true) @RequestParam List<String> codes) {
        List<Map<String, Object>> dictItems = entityService.listByParentCodes(codes);
        return R.ok().data(R.ITEMS, dictItems.stream().collect(Collectors.groupingBy(g -> g.get("pCode"))));
    }

}

