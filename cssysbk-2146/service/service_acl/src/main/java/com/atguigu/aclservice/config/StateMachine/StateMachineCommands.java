package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.enums.Events;
import com.atguigu.aclservice.enums.States;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import reactor.core.publisher.Mono;

@ShellComponent
public class StateMachineCommands extends AbstractStateMachineCommands<States, Events> {

    @ShellMethod(value = "sm event -- Sends an event to a state machine")
    public String event(@ShellOption(value = { "", "event" }, help = "The event") final Events event) {
        getStateMachine()
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload(event).build()))
                .subscribe();
        return "Event " + event + " send";
    }

}
