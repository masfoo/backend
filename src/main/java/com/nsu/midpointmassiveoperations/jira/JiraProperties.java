package com.nsu.midpointmassiveoperations.jira;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "jira")
@Getter
@Setter
public class JiraProperties {

    private String baseUrl;
    private String apiPath;
    private String login;
    private String password;
}
