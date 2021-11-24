package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.entity.bus.BusTaskForm;
import com.atguigu.aclservice.enums.ComplexFormEvents;
import com.atguigu.aclservice.enums.ComplexFormStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class ComplexFormCheckChoiceGuard implements Guard<ComplexFormStates, ComplexFormEvents> {

    @Override
    public boolean evaluate(StateContext<ComplexFormStates, ComplexFormEvents> context) {
        boolean returnValue = false;
        BusTaskForm form = context.getMessage().getHeaders().get("form", BusTaskForm.class);
        if (form.getContent() == null) {
            returnValue = false;
        } else {
            returnValue = true;
        }
        return returnValue;
    }

}

