package com.nsu.midpointmassiveoperations;

import com.nsu.midpointmassiveoperations.jira.JiraProperties;
import com.nsu.midpointmassiveoperations.midpoint.MidpointProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({JiraProperties.class, MidpointProperties.class})
public class MidpointMassiveOperationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MidpointMassiveOperationsApplication.class, args);
    }

}
