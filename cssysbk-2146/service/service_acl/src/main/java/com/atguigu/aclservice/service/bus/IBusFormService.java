package com.atguigu.aclservice.service.bus;

import com.atguigu.aclservice.entity.bus.BusForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 业务表单 服务类
 * </p>
 *
 * @author zwx
 * @since 2021-11-29
 */
public interface IBusFormService extends IService<BusForm> {

    void saveCus(Map<String, Object> params);

}
