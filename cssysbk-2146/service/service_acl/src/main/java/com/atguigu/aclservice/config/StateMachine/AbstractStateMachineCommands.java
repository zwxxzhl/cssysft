package com.atguigu.aclservice.config.StateMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

@ShellComponent
public class AbstractStateMachineCommands<S, E>{

    @Autowired(required = false)
    private StateMachine<S, E> stateMachine;

    protected StateMachine<S, E> getStateMachine() {
        return stateMachine;
    }

    @Autowired(required = false)
    @Qualifier("stateChartModel")
    private String stateChartModel;

    @ShellMethod(value = "sm state -- Prints current state")
    public String state() {
        State<S, E> state = stateMachine.getState();
        if (state != null) {
            return StringUtils.collectionToCommaDelimitedString(state.getIds());
        } else {
            return "No state";
        }
    }

    @ShellMethod(value = "sm start -- Start a state machine")
    public String start() {
        stateMachine.startReactively().subscribe();
        return "State machine started";
    }

    @ShellMethod(value = "sm stop -- Stop a state machine")
    public String stop() {
        stateMachine.stopReactively().subscribe();
        return "State machine stopped";
    }

    @ShellMethod(value = "sm print -- Print state machine")
    public String print() {
        return stateChartModel;
    }

    @ShellMethod(value = "sm variables -- Prints extended state variables")
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
