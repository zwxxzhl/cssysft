package com.atguigu.aclservice.controller.statemachine;

import com.atguigu.aclservice.enums.RefundReasonEvents;
import com.atguigu.aclservice.enums.RefundReasonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/acl/flow")
public class StateMachineController {

    @Autowired(required = false)
    private StateMachine<RefundReasonStatus, RefundReasonEvents> stateMachine;

    @GetMapping("/machine")
    public void testMachine() {
        stateMachine.start();
        stateMachine.sendEvent(RefundReasonEvents.APPROVE);
    }

}
