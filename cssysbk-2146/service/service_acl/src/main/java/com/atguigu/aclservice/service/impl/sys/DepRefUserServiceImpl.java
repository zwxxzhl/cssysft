package com.atguigu.aclservice.service.impl.sys;

import com.atguigu.aclservice.entity.sys.DepRefUser;
import com.atguigu.aclservice.mapper.sys.DepRefUserMapper;
import com.atguigu.aclservice.service.sys.IDepRefUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门与用户关系表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-08-16
 */
@Service
public class DepRefUserServiceImpl extends ServiceImpl<DepRefUserMapper, DepRefUser> implements IDepRefUserService {

}
