package com.atguigu.aclservice.config.StateMachine;

import com.atguigu.aclservice.enums.Events;
import com.atguigu.aclservice.enums.States;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class StateMachineCommands extends AbstractStateMachineCommands<States, Events> {

    @CliCommand(value = "sm event", help = "Sends an event to a state machine")
    public String event(@CliOption(key = { "", "event" }, mandatory = true, help = "The event") final Events event) {
        getStateMachine()
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload(event).build()))
                .subscribe();
        return "Event " + event + " send";
    }

}
