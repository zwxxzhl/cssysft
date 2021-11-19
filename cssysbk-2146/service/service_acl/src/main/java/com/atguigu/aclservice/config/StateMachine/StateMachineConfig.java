package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.enums.RefundReasonEvents;
import com.atguigu.aclservice.enums.RefundReasonStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.transition.Transition;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<RefundReasonStatus, RefundReasonEvents> {

    /**
     * @param
     * @return
     * @Author xyhua
     * @Description 用来初始化当前状态机拥有哪些状态
     * @Date 16:54 2020-04-13
     **/
    @Override
    public void configure(StateMachineStateConfigurer<RefundReasonStatus, RefundReasonEvents> states) throws Exception {
        states.withStates()
                .initial(RefundReasonStatus.READY_TO_APPROVE)
                .states(EnumSet.allOf(RefundReasonStatus.class));
    }

    /**
     * @param
     * @return
     * @Author xyhua
     * @Description 初始化当前状态机有哪些状态迁移动作，其中命名中我们很容易理解每一个迁移动作
     * @Date 18:29 2020-04-13
     **/
    @Override
    public void configure(StateMachineTransitionConfigurer<RefundReasonStatus, RefundReasonEvents> transitions) throws Exception {

        transitions
                .withExternal()
                .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.APPROVED)
                .event(RefundReasonEvents.APPROVE)
                .and()
                .withExternal()
                .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.REJECT)
                .event(RefundReasonEvents.REJECT)
                .and()
                .withExternal()
                .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.AUTO_REJECT)
                .event(RefundReasonEvents.TIME_OUT);
    }

    /**
     * @param
     * @return
     * @Author xyhua
     * @Description 指定状态机的处理监听器
     * @Date 18:29 2020-04-13
     **/
    @Override
    public void configure(StateMachineConfigurationConfigurer<RefundReasonStatus, RefundReasonEvents> config)
            throws Exception {
        config
                .withConfiguration()
                .listener(listener());
    }

    /**
     * @param
     * @return
     * @Author xyhua
     * @Description 状态机监听器
     * @Date 18:30 2020-04-13
     **/
    @Bean
    public StateMachineListener<RefundReasonStatus, RefundReasonEvents> listener() {
        return new StateMachineListenerAdapter<RefundReasonStatus, RefundReasonEvents>() {
            @Override
            public void transition(Transition<RefundReasonStatus, RefundReasonEvents> transition) {
                if (transition.getTarget().getId() == RefundReasonStatus.READY_TO_APPROVE) {
                    log.info("待审批");
                    return;
                }

                if (transition.getSource().getId() == RefundReasonStatus.READY_TO_APPROVE
                        && transition.getTarget().getId() == RefundReasonStatus.APPROVED) {
                    log.info("审批通过");
                    return;
                }

                if (transition.getSource().getId() == RefundReasonStatus.READY_TO_APPROVE
                        && transition.getTarget().getId() == RefundReasonStatus.REJECT) {
                    log.info("审批拒绝");
                    return;
                }

                if (transition.getSource().getId() == RefundReasonStatus.READY_TO_APPROVE
                        && transition.getTarget().getId() == RefundReasonStatus.AUTO_REJECT) {
                    log.info("自动审批拒绝");
                    return;
                }
            }
        };
    }
}

