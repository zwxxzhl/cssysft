package com.atguigu.aclservice.service.sys.impl;

import com.atguigu.aclservice.entity.sys.DictItem;
import com.atguigu.aclservice.mapper.sys.mapper.DictItemMapper;
import com.atguigu.aclservice.service.sys.IDictItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典明细表 服务实现类
 * </p>
 *
 * @author zwx
 * @since 2021-10-25
 */
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements IDictItemService {

    @Override
    public List<Map<String, Object>> listByParentCodes(List<String> codes) {
        return baseMapper.listByParentCodes(codes);
    }
}
