package com.atguigu.aclservice.config.StateMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class TasksCommands {

    @Autowired
    private Tasks tasks;

    @ShellMethod(value = "tasks run")
    public void run() {
        tasks.run();
    }

    @ShellMethod(value = "tasks list")
    public String list() {
        return tasks.toString();
    }

    @ShellMethod(value = "tasks fix")
    public void fix() {
        tasks.fix();
    }

    @ShellMethod(value = "tasks fail")
    public void fail(@ShellOption(value = {"", "task"}, help = "Task id") String task) {
        tasks.fail(task);
    }

}
