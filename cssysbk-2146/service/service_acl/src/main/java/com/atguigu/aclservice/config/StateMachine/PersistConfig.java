package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.enums.RefundReasonEvents;
import com.atguigu.aclservice.enums.RefundReasonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@Configuration
public class PersistConfig {

    @Autowired
    private OrderStateMachinePersist orderStateMachinePersist;

    /**
     * 注入StateMachinePersister对象
     */
    @Bean(name="orderPersister")
    public StateMachinePersister<RefundReasonStatus, RefundReasonEvents, Integer> orderPersister() {
        return new DefaultStateMachinePersister<>(orderStateMachinePersist);
    }

}

