package com.nsu.midpointmassiveoperations.jira.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jira")
@Getter
@Setter
public class JiraProperties {

    private String baseUrl;
    private String apiPath;
    private String login;
    private String accessToken;
    private String filterTaskKey;
}
