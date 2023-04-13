package com.nsu.midpointmassiveoperations.midpoint;

import com.nsu.midpointmassiveoperations.exception.MidpointDoesntResponseException;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class MidpointClientConfig {

    @Bean
    public MidpointProperties midpointProperties() {
        return new MidpointProperties();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(MidpointProperties midpointProperties) {
        return new BasicAuthRequestInterceptor(midpointProperties.getLogin(), midpointProperties.getPassword());
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> new MidpointDoesntResponseException("midpoint doesnt response");
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
