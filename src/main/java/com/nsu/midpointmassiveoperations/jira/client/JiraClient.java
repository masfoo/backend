package com.nsu.midpointmassiveoperations.jira.client;

import com.nsu.midpointmassiveoperations.jira.client.JiraClientConfig;
import com.nsu.midpointmassiveoperations.jira.model.Issue;
import com.nsu.midpointmassiveoperations.jira.model.IssuesResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "jira", url = "${jira.base-url}" + "${jira.api-path}", configuration = JiraClientConfig.class)
public interface JiraClient {

    @GetMapping(value = "/issue/{key}", produces = "application/json")
    Issue getIssue(@PathVariable(value = "key") String key);

    @PutMapping(value = "/issue/{issueKey}", consumes = "application/json")
    void updateCandidateIssue(@RequestBody Issue request, @PathVariable(value = "issueKey") String issueKey);

    @GetMapping(value = "/search?jql=parent={parent-key}")
    IssuesResult findSubIssues(@PathVariable(value = "parent-key") String parentKey);

}
