package com.atguigu.aclservice.service.sys.impl;

import com.atguigu.aclservice.entity.sys.Dep;
import com.atguigu.aclservice.mapper.sys.mapper.DepMapper;
import com.atguigu.aclservice.service.sys.IDepService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-08-16
 */
@Service
public class DepServiceImpl extends ServiceImpl<DepMapper, Dep> implements IDepService {

}
