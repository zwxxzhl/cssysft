package com.atguigu.aclservice.service.sys;

import com.atguigu.aclservice.entity.sys.DictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典明细表 服务类
 * </p>
 *
 * @author zwx
 * @since 2021-10-25
 */
public interface IDictItemService extends IService<DictItem> {

    List<Map<String, Object>> listByParentCodes(List<String> codes);
}
