package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.Dep;
import com.atguigu.aclservice.mapper.DepMapper;
import com.atguigu.aclservice.service.IDepService;
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
