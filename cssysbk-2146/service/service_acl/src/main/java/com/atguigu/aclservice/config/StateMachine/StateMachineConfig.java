package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.enums.Events;
import com.atguigu.aclservice.enums.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.region.RegionExecutionPolicy;
import org.springframework.statemachine.state.State;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.READY)
                .fork(States.FORK)
                .state(States.TASKS)
                .join(States.JOIN)
                .choice(States.CHOICE)
                .state(States.ERROR)
                .and()
                .withStates()
                .parent(States.TASKS)
                .initial(States.T1)
                .end(States.T1E)
                .and()
                .withStates()
                .parent(States.TASKS)
                .initial(States.T2)
                .end(States.T2E)
                .and()
                .withStates()
                .parent(States.TASKS)
                .initial(States.T3)
                .end(States.T3E)
                .and()
                .withStates()
                .parent(States.ERROR)
                .initial(States.AUTOMATIC)
                .state(States.AUTOMATIC, automaticAction(), null)
                .state(States.MANUAL);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.READY).target(States.FORK)
                .event(Events.RUN)
                .and()
                .withFork()
                .source(States.FORK).target(States.TASKS)
                .and()
                .withExternal()
                .source(States.T1).target(States.T1E)
                .and()
                .withExternal()
                .source(States.T2).target(States.T2E)
                .and()
                .withExternal()
                .source(States.T3).target(States.T3E)
                .and()
                .withJoin()
                .source(States.TASKS).target(States.JOIN)
                .and()
                .withExternal()
                .source(States.JOIN).target(States.CHOICE)
                .and()
                .withChoice()
                .source(States.CHOICE)
                .first(States.ERROR, tasksChoiceGuard())
                .last(States.READY)
                .and()
                .withExternal()
                .source(States.ERROR).target(States.READY)
                .event(Events.CONTINUE)
                .and()
                .withExternal()
                .source(States.AUTOMATIC).target(States.MANUAL)
                .event(Events.FALLBACK)
                .and()
                .withInternal()
                .source(States.MANUAL)
                .action(fixAction())
                .event(Events.FIX);
    }

    @Bean
    public Guard<States, Events> tasksChoiceGuard() {
        return new Guard<States, Events>() {

            @Override
            public boolean evaluate(StateContext<States, Events> context) {
                Map<Object, Object> variables = context.getExtendedState().getVariables();
                return !(ObjectUtils.nullSafeEquals(variables.get("T1"), true)
                        && ObjectUtils.nullSafeEquals(variables.get("T2"), true)
                        && ObjectUtils.nullSafeEquals(variables.get("T3"), true));
            }
        };
    }

    @Bean
    public Action<States, Events> automaticAction() {
        return new Action<States, Events>() {

            @Override
            public void execute(StateContext<States, Events> context) {
                Map<Object, Object> variables = context.getExtendedState().getVariables();
                if (ObjectUtils.nullSafeEquals(variables.get("T1"), true)
                        && ObjectUtils.nullSafeEquals(variables.get("T2"), true)
                        && ObjectUtils.nullSafeEquals(variables.get("T3"), true)) {
                    context.getStateMachine()
                            .sendEvent(Mono.just(MessageBuilder
                                    .withPayload(Events.CONTINUE).build()))
                            .subscribe();
                } else {
                    context.getStateMachine()
                            .sendEvent(Mono.just(MessageBuilder
                                    .withPayload(Events.FALLBACK).build()))
                            .subscribe();
                }
            }
        };
    }

    @Bean
    public Action<States, Events> fixAction() {
        return new Action<States, Events>() {

            @Override
            public void execute(StateContext<States, Events> context) {
                Map<Object, Object> variables = context.getExtendedState().getVariables();
                variables.put("T1", true);
                variables.put("T2", true);
                variables.put("T3", true);
                context.getStateMachine()
                        .sendEvent(Mono.just(MessageBuilder
                                .withPayload(Events.CONTINUE).build()))
                        .subscribe();
            }
        };
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .regionExecutionPolicy(RegionExecutionPolicy.PARALLEL);
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<States, Events>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                System.out.println("State change to " + to.getId());
            }
        };
    }
}















// package com.atguigu.aclservice.config.StateMachine;
//
// import com.atguigu.aclservice.enums.RefundReasonEvents;
// import com.atguigu.aclservice.enums.RefundReasonStatus;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.statemachine.config.EnableStateMachine;
// import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
// import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
// import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
// import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
// import org.springframework.statemachine.listener.StateMachineListener;
// import org.springframework.statemachine.listener.StateMachineListenerAdapter;
// import org.springframework.statemachine.transition.Transition;
//
// import java.util.EnumSet;
//
// @Slf4j
// @Configuration
// @EnableStateMachine
// public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<RefundReasonStatus, RefundReasonEvents> {
//
//     /**
//      * @param
//      * @return
//      * @Author xyhua
//      * @Description 用来初始化当前状态机拥有哪些状态
//      * @Date 16:54 2020-04-13
//      **/
//     @Override
//     public void configure(StateMachineStateConfigurer<RefundReasonStatus, RefundReasonEvents> states) throws Exception {
//         states.withStates()
//                 .initial(RefundReasonStatus.READY_TO_APPROVE)
//                 .states(EnumSet.allOf(RefundReasonStatus.class));
//     }
//
//     /**
//      * @param
//      * @return
//      * @Author xyhua
//      * @Description 初始化当前状态机有哪些状态迁移动作，其中命名中我们很容易理解每一个迁移动作
//      * @Date 18:29 2020-04-13
//      **/
//     @Override
//     public void configure(StateMachineTransitionConfigurer<RefundReasonStatus, RefundReasonEvents> transitions) throws Exception {
//
//         transitions
//                 .withExternal()
//                 .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.APPROVED)
//                 .event(RefundReasonEvents.APPROVE)
//                 .and()
//                 .withExternal()
//                 .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.REJECT)
//                 .event(RefundReasonEvents.REJECT)
//                 .and()
//                 .withExternal()
//                 .source(RefundReasonStatus.READY_TO_APPROVE).target(RefundReasonStatus.AUTO_REJECT)
//                 .event(RefundReasonEvents.TIME_OUT);
//     }
//
//     /**
//      * @param
//      * @return
//      * @Author xyhua
//      * @Description 指定状态机的处理监听器
//      * @Date 18:29 2020-04-13
//      **/
//     @Override
//     public void configure(StateMachineConfigurationConfigurer<RefundReasonStatus, RefundReasonEvents> config)
//             throws Exception {
//         config
//                 .withConfiguration()
//                 .listener(listener());
//     }
//
//     /**
//      * @param
//      * @return
//      * @Author xyhua
//      * @Description 状态机监听器
//      * @Date 18:30 2020-04-13
//      **/
//     @Bean
//     public StateMachineListener<RefundReasonStatus, RefundReasonEvents> listener() {
//         return new StateMachineListenerAdapter<RefundReasonStatus, RefundReasonEvents>() {
//             @Override
//             public void transition(Transition<RefundReasonStatus, RefundReasonEvents> transition) {
//                 if (transition.getTarget().getId() == RefundReasonStatus.READY_TO_APPROVE) {
//                     log.info("待审批");
//                     return;
//                 }
//
//                 if (transition.getSource().getId() == RefundReasonStatus.READY_TO_APPROVE
//                         && transition.getTarget().getId() == RefundReasonStatus.APPROVED) {
//                     log.info("审批通过");
//                     return;
//                 }
//
//                 if (transition.getSource().getId() == RefundReasonStatus.READY_TO_APPROVE
//                         && transition.getTarget().getId() == RefundReasonStatus.REJECT) {
//                     log.info("审批拒绝");
//                     return;
//                 }
//
//                 if (transition.getSource().getId() == RefundReasonStatus.READY_TO_APPROVE
//                         && transition.getTarget().getId() == RefundReasonStatus.AUTO_REJECT) {
//                     log.info("自动审批拒绝");
//                     return;
//                 }
//             }
//         };
//     }
// }
//
