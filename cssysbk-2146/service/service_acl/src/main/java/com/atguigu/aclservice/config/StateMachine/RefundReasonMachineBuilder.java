package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.enums.RefundReasonEvents;
import com.atguigu.aclservice.enums.RefundReasonStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Slf4j
@Component
public class RefundReasonMachineBuilder {

    public StateMachine<RefundReasonStatus, RefundReasonEvents> build(BeanFactory beanFactory) throws Exception {
        StateMachineBuilder.Builder<RefundReasonStatus, RefundReasonEvents> builder = StateMachineBuilder.builder();
        log.info("构造退款原因修改状态机");
        builder.configureConfiguration()
                .withConfiguration()
                .machineId("refundReasonMachine")
                .beanFactory(beanFactory);

        /*
          设置初始值和状态列表
         */
        builder.configureStates().withStates()
                .initial(RefundReasonStatus.READY_TO_APPROVE)
                .states(EnumSet.allOf(RefundReasonStatus.class));

        /*
          设置动作和状态变更
         */
        builder.configureTransitions()
                .withExternal()
                .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.APPROVED)
                .event(RefundReasonEvents.APPROVE).action(action())
                .and()
                .withExternal()
                .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.REJECT)
                .event(RefundReasonEvents.REJECT)
                .and()
                .withExternal()
                .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.AUTO_REJECT)
                .event(RefundReasonEvents.TIME_OUT);

        return builder.build();
    }

    public Action<RefundReasonStatus, RefundReasonEvents> action() {
        return (stateContext) -> log.info("action:{}", stateContext);
    }
}

