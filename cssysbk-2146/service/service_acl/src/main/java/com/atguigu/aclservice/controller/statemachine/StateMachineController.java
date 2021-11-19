package com.atguigu.aclservice.controller.statemachine;

import com.atguigu.aclservice.config.StateMachine.RefundReasonMachineBuilder;
import com.atguigu.aclservice.enums.RefundReasonEvents;
import com.atguigu.aclservice.enums.RefundReasonStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/acl/flow")
public class StateMachineController {

    @Autowired(required = false)
    private StateMachine<RefundReasonStatus, RefundReasonEvents> stateMachine;

    @Autowired
    private RefundReasonMachineBuilder refundReasonMachineBuilder;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    @Qualifier("orderPersister")
    private StateMachinePersister<RefundReasonStatus, RefundReasonEvents, Integer> persister;

    @GetMapping("/machine")
    public void testMachine() throws Exception {
        System.out.println("======================初始化方法===========================");

        StateMachine<RefundReasonStatus, RefundReasonEvents> stateMachine = refundReasonMachineBuilder.build(beanFactory);

        persister.restore(stateMachine, 1);
        //查看恢复后状态机的状态
        System.out.println("恢复后的状态：" + stateMachine.getState().getId());

        //发送订单信息
        Map<String, Object> map = new HashMap<>();
        map.put("orderSn", "orderSn");
        map.put("mobile", "13613650996");
        map.put("skuId", 98765L);
        Message<RefundReasonEvents> message = MessageBuilder.withPayload(RefundReasonEvents.APPROVE)
                .setHeader("order", map).build();
        stateMachine.sendEvent(message);
        log.info("最终状态：" + stateMachine.getState().getId());


        /*StateMachine<RefundReasonStatus, RefundReasonEvents> stateMachine = refundReasonMachineBuilder.build(beanFactory);
        stateMachine.start();
        //发送订单信息
        Map<String, Object> map = new HashMap<>();
        map.put("orderSn", "orderSn");
        map.put("mobile", "13613650996");
        map.put("skuId", 98765L);

        Message<RefundReasonEvents> message = MessageBuilder.withPayload(RefundReasonEvents.APPROVE)
                .setHeader("order", map).build();
        stateMachine.sendEvent(message);
        log.info("最终状态：" + stateMachine.getState().getId());*/


        /*StateMachine<RefundReasonStatus, RefundReasonEvents> stateMachine = refundReasonMachineBuilder.build(beanFactory);
        stateMachine.start();
        stateMachine.sendEvent(RefundReasonEvents.APPROVE);
        log.info("最终状态：" + stateMachine.getState().getId());*/

        /*stateMachine.start();
        stateMachine.sendEvent(RefundReasonEvents.REJECT);*/

        /*stateMachine.startReactively();
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(RefundReasonEvents.APPROVE).build())).subscribe();*/
    }

}
