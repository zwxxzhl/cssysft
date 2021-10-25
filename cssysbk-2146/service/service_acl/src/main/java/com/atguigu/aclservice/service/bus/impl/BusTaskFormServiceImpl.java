package com.atguigu.aclservice.service.bus.impl;

import com.atguigu.aclservice.entity.bus.BusTaskForm;
import com.atguigu.aclservice.mapper.bus.mapper.BusTaskFormMapper;
import com.atguigu.aclservice.service.bus.IBusTaskFormService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 派发任务表单 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-05-31
 */
@Service
public class BusTaskFormServiceImpl extends ServiceImpl<BusTaskFormMapper, BusTaskForm> implements IBusTaskFormService {

}
