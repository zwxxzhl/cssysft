package com.atguigu.aclservice.mapper.sys.mapper;

import com.atguigu.aclservice.entity.sys.DictItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典明细表 Mapper 接口
 * </p>
 *
 * @author zwx
 * @since 2021-10-25
 */
public interface DictItemMapper extends BaseMapper<DictItem> {

    List<Map<String, Object>> listByParentCodes(@Param("codes") List<String> codes);

}
