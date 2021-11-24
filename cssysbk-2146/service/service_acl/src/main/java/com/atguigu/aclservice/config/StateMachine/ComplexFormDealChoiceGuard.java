package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.entity.bus.BusTaskForm;
import com.atguigu.aclservice.enums.ComplexFormEvents;
import com.atguigu.aclservice.enums.ComplexFormStates;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class ComplexFormDealChoiceGuard implements Guard<ComplexFormStates, ComplexFormEvents> {

    @Override
    public boolean evaluate(StateContext<ComplexFormStates, ComplexFormEvents> context) {
        System.out.println("ComplexFormDealChoiceGuard!!!!!!!!!!!!!");
        boolean returnValue = false;
        BusTaskForm form = context.getMessage().getHeaders().get("form", BusTaskForm.class);

        if ((form.getContent() == null)||(form.getContent().indexOf("坏") > -1)) {
            returnValue = false;
        } else {
            returnValue = true;
        }

        System.out.println(form.toString()+" is "+returnValue);
        return returnValue;
    }

}

