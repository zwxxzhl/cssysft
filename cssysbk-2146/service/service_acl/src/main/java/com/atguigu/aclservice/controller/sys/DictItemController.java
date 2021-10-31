package com.atguigu.aclservice.controller.sys;


import com.atguigu.aclservice.entity.sys.DictItem;
import com.atguigu.aclservice.service.sys.IDictItemService;
import com.atguigu.aclservice.util.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        super.UNI_COLUMN = "code";
        super.WRAPPER_ADD_DELETE = false;
        super.DELETE_COLUMN = "is_deleted";
    }

}

