package com.atguigu.aclservice;

import com.atguigu.aclservice.enums.Events;
import com.atguigu.aclservice.enums.States;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.atguigu")
@MapperScan("com.atguigu.aclservice.mapper.**.mapper")
public class ServiceAclApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAclApplication.class, args);
    }

    @Autowired(required = false)
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("======================初始化方法===========================");
        // stateMachine.startReactively();
        // stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.RUN).build()));
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.RUN).build())).subscribe();
    }
}
