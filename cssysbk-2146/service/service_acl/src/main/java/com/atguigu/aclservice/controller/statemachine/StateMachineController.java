package com.atguigu.aclservice.controller.statemachine;

import com.atguigu.aclservice.config.StateMachine.ComplexFormStateMachineBuilder;
import com.atguigu.aclservice.config.StateMachine.RefundReasonMachineBuilder;
import com.atguigu.aclservice.entity.bus.BusTaskForm;
import com.atguigu.aclservice.enums.ComplexFormEvents;
import com.atguigu.aclservice.enums.ComplexFormStates;
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

    @Autowired
    private ComplexFormStateMachineBuilder complexFormStateMachineBuilder;

    @GetMapping("/machine")
    public void testMachine() throws Exception {
        System.out.println("======================初始化方法===========================");

        StateMachine<ComplexFormStates, ComplexFormEvents> stateMachine = complexFormStateMachineBuilder.build(beanFactory);
        System.out.println(stateMachine.getId());

        BusTaskForm form1 = new BusTaskForm();
        form1.setId("111");
        form1.setContent(null);

        BusTaskForm form2 = new BusTaskForm();
        form2.setId("222");
        form2.setContent("好的表单");

        BusTaskForm form3 = new BusTaskForm();
        form3.setId("333");
        form3.setContent(null);

        // 创建流程
        System.out.println("-------------------form1------------------");

        stateMachine.start();
        Message message = MessageBuilder.withPayload(ComplexFormEvents.WRITE).setHeader("form", form1).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.CHECK).setHeader("form", form1).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.DEAL).setHeader("form", form1).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.SUBMIT).setHeader("form", form1).build();
        stateMachine.sendEvent(message);
        System.out.println("最终状态：" + stateMachine.getState().getId());

        System.out.println("-------------------form2------------------");
        stateMachine = complexFormStateMachineBuilder.build(beanFactory);
        stateMachine.start();
        message = MessageBuilder.withPayload(ComplexFormEvents.WRITE).setHeader("form", form2).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.CHECK).setHeader("form", form2).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.DEAL).setHeader("form", form2).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.SUBMIT).setHeader("form", form2).build();
        stateMachine.sendEvent(message);
        System.out.println("最终状态：" + stateMachine.getState().getId());

        System.out.println("-------------------form3------------------");
        stateMachine = complexFormStateMachineBuilder.build(beanFactory);
        stateMachine.start();
        message = MessageBuilder.withPayload(ComplexFormEvents.WRITE).setHeader("form", form3).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.CHECK).setHeader("form", form3).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        form3.setContent("好的表单");
        message = MessageBuilder.withPayload(ComplexFormEvents.DEAL).setHeader("form", form3).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.SUBMIT).setHeader("form", form3).build();
        stateMachine.sendEvent(message);
        message = MessageBuilder.withPayload(ComplexFormEvents.CHECK).setHeader("form", form3).build();
        stateMachine.sendEvent(message);
        System.out.println("当前状态：" + stateMachine.getState().getId());
        message = MessageBuilder.withPayload(ComplexFormEvents.SUBMIT).setHeader("form", form3).build();
        stateMachine.sendEvent(message);
        System.out.println("最终状态：" + stateMachine.getState().getId());



        /*StateMachine<RefundReasonStatus, RefundReasonEvents> stateMachine = refundReasonMachineBuilder.build(beanFactory);

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
        log.info("最终状态：" + stateMachine.getState().getId());*/


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
