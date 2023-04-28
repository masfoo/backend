package com.nsu.midpointmassiveoperations.jira.client;

import com.nsu.midpointmassiveoperations.jira.client.JiraClientConfig;
import com.nsu.midpointmassiveoperations.jira.model.Issue;
import com.nsu.midpointmassiveoperations.jira.model.IssuesResult;
import com.nsu.midpointmassiveoperations.jira.model.JiraChangeIssueStatus;
import com.nsu.midpointmassiveoperations.jira.model.JiraIssueAvailableStatuses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "jira", url = "${jira.base-url}" + "${jira.api-path}", configuration = JiraClientConfig.class)
public interface JiraClient {

    @GetMapping(value = "/issue/{key}", produces = "application/json")
    Issue getIssue(@PathVariable(value = "key") String key);

    @PostMapping(value = "/issue/{key}/transitions")
    void changeIssueStatus(@PathVariable(value = "key") String key, @RequestBody JiraChangeIssueStatus changeStatusTo);

    @GetMapping(value = "/search?jql=parent={parent-key}")
    IssuesResult findSubIssues(@PathVariable(value = "parent-key") String parentKey);

    @GetMapping(value = "/issue/{key}/transitions")
    JiraIssueAvailableStatuses findAvailableStatusesOfIssue(@PathVariable("key") String key);

}
