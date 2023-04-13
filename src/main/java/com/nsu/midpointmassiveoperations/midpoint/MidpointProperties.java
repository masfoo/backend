package com.nsu.midpointmassiveoperations.midpoint;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "midpoint")
@Getter
@Setter
public class MidpointProperties {

    private String baseUrl;
    private String login;
    private String password;
}
