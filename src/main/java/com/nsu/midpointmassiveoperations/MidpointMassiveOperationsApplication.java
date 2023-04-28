package com.nsu.midpointmassiveoperations;

import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties({JiraProperties.class, MidpointProperties.class})
public class MidpointMassiveOperationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidpointMassiveOperationsApplication.class, args);
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
