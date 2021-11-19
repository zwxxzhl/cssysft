package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.enums.RefundReasonEvents;
import com.atguigu.aclservice.enums.RefundReasonStatus;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachinePersist implements StateMachinePersist<RefundReasonStatus, RefundReasonEvents, Integer> {

    @Override
    public void write(StateMachineContext<RefundReasonStatus, RefundReasonEvents> context, Integer state) {
        //这里不做任何持久化工作
    }

    @Override
    public StateMachineContext<RefundReasonStatus, RefundReasonEvents> read(Integer state) {
        return new DefaultStateMachineContext<>(RefundReasonStatus.valueOf(state),
                        null, null, null, null, "refundReasonMachine");
    }
}

