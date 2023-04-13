package com.nsu.midpointmassiveoperations.jira;

import com.nsu.midpointmassiveoperations.jira.model.Issue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "jira", url = "${jira.base-url}" + "${jira.api-path}", configuration = JiraClientConfig.class)
public interface JiraClient {

    @GetMapping(value = "/issue/{key}", produces = "application/json")
    Issue getIssue(@PathVariable(value = "key") String key);

}
