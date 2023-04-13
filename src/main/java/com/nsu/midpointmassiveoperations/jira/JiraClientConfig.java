package com.nsu.midpointmassiveoperations.jira;

import com.nsu.midpointmassiveoperations.exception.JiraDoesntResponseException;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;


public class JiraClientConfig {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(JiraProperties jiraProperties) {
        return new BasicAuthRequestInterceptor(jiraProperties.getLogin(), jiraProperties.getPassword());
    }

    @Bean
    public JiraProperties jiraProperties() {
        return new JiraProperties();
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
