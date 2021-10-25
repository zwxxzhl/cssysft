package com.atguigu.aclservice.service.sys.impl;

import com.atguigu.aclservice.entity.sys.Dict;
import com.atguigu.aclservice.mapper.sys.mapper.DictMapper;
import com.atguigu.aclservice.service.sys.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典类别表 服务实现类
 * </p>
 *
 * @author zwx
 * @since 2021-10-25
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

}
