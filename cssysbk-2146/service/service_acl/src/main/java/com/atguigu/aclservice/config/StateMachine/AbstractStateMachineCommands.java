package com.atguigu.aclservice.config.StateMachine;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AbstractStateMachineCommands<S, E> implements CommandMarker{

    @Autowired
    private StateMachine<S, E> stateMachine;

    protected StateMachine<S, E> getStateMachine() {
        return stateMachine;
    }

    @Autowired
    @Qualifier("stateChartModel")
    private String stateChartModel;

    @CliCommand(value = "sm state", help = "Prints current state")
    public String state() {
        State<S, E> state = stateMachine.getState();
        if (state != null) {
            return StringUtils.collectionToCommaDelimitedString(state.getIds());
        } else {
            return "No state";
        }
    }

    @CliCommand(value = "sm start", help = "Start a state machine")
    public String start() {
        stateMachine.startReactively().subscribe();
        return "State machine started";
    }

    @CliCommand(value = "sm stop", help = "Stop a state machine")
    public String stop() {
        stateMachine.stopReactively().subscribe();
        return "State machine stopped";
    }

    @CliCommand(value = "sm print", help = "Print state machine")
    public String print() {
        return stateChartModel;
    }

    @CliCommand(value = "sm variables", help = "Prints extended state variables")
    public String variables() {
        StringBuilder buf = new StringBuilder();
        Set<Entry<Object, Object>> entrySet = stateMachine.getExtendedState().getVariables().entrySet();
        Iterator<Entry<Object, Object>> iterator = entrySet.iterator();
        if (entrySet.size() > 0) {
            while (iterator.hasNext()) {
                Entry<Object, Object> e = iterator.next();
                buf.append(e.getKey() + "=" + e.getValue());
                if (iterator.hasNext()) {
                    buf.append("\n");
                }
            }
        } else {
            buf.append("No variables");
        }
        return buf.toString();
    }
}
