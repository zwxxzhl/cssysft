package com.atguigu.aclservice.service.bus.impl;

import com.atguigu.aclservice.entity.bus.BusForm;
import com.atguigu.aclservice.entity.bus.BusFormRefUser;
import com.atguigu.aclservice.entity.sys.User;
import com.atguigu.aclservice.enums.FormStatusEnum;
import com.atguigu.aclservice.enums.TimeStatusEnum;
import com.atguigu.aclservice.enums.UserFormStatusEnum;
import com.atguigu.aclservice.enums.UserTimeStatusEnum;
import com.atguigu.aclservice.mapper.bus.mapper.BusFormMapper;
import com.atguigu.aclservice.mapper.bus.mapper.BusFormRefUserMapper;
import com.atguigu.aclservice.service.bus.IBusFormService;
import com.atguigu.aclservice.service.sys.UserService;
import com.atguigu.aclservice.util.AuxProUtil;
import com.atguigu.aclservice.util.JacksonCusUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 业务表单 服务实现类
 * </p>
 *
 * @author zwx
 * @since 2021-11-29
 */
@Service
public class BusFormServiceImpl extends ServiceImpl<BusFormMapper, BusForm> implements IBusFormService {

    protected final UserService userService;
    protected final BusFormMapper busFormMapper;
    protected final BusFormRefUserMapper busFormRefUserMapper;

    @Autowired(required = false)
    public BusFormServiceImpl(UserService userService, BusFormMapper busFormMapper, BusFormRefUserMapper busFormRefUserMapper) {
        this.userService = userService;
        this.busFormMapper = busFormMapper;
        this.busFormRefUserMapper = busFormRefUserMapper;
    }

    /**
     * 保存业务表单
     */
    @Override
    public void saveCus(Map<String, Object> params) {
        try {
            User loginUser = AuxProUtil.getLoginUser(userService);
            if (!Optional.ofNullable(loginUser).isPresent()) {
                throw new RuntimeException("用户未登录");
            }
            // 业务表单
            BusForm busForm = JacksonCusUtil.mapToBean(params, BusForm.class);
            if (!Optional.ofNullable(busForm).isPresent()) {
                throw new RuntimeException("表单数据为空");
            }

            busForm.setPid("0");
            busForm.setUserId(loginUser.getId());
            busForm.setFormStatus(FormStatusEnum.REPORTING.getCode());
            busForm.setTimeStatus(TimeStatusEnum.NORMAL.getCode());
            AuxProUtil.bindEntityCreate(busForm, loginUser);
            busFormMapper.insert(busForm);

            // 接收者
            if (Optional.ofNullable(params.get("userIds")).isPresent() && StringUtils.hasText(params.get("userIds").toString())) {
                String ids = params.get("userIds").toString();
                String[] split = ids.split(",");
                for (String userId : split) {
                    BusFormRefUser busFormRefUser = new BusFormRefUser();
                    busFormRefUser.setFormId(busForm.getId());
                    busFormRefUser.setUserId(userId);
                    busFormRefUser.setUserFormStatus(UserFormStatusEnum.NOT_REPORT.getCode());
                    busFormRefUser.setUserTimeStatus(UserTimeStatusEnum.NORMAL.getCode());
                    AuxProUtil.bindEntityCreate(busForm, loginUser);
                    busFormRefUserMapper.insert(busFormRefUser);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("新增出错");
        }
    }
}
