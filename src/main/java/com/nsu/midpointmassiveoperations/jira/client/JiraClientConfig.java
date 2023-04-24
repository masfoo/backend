package com.nsu.midpointmassiveoperations.jira.client;

import com.nsu.midpointmassiveoperations.exception.JiraDoesntResponseException;
import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class JiraClientConfig {

    @Bean
    public RequestInterceptor authInterceptor(JiraProperties jiraProperties) {
        return requestTemplate -> {
            String email = jiraProperties.getLogin();
            String apiToken = jiraProperties.getAccessToken();
            String authString = email + ":" + apiToken;
            String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
            requestTemplate.header("Authorization", "Basic " + encodedAuthString);
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> new JiraDoesntResponseException("jira does not response");
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
