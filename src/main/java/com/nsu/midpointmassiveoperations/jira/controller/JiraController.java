package com.nsu.midpointmassiveoperations.jira.controller;

import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.jira.client.JiraClient;
import com.nsu.midpointmassiveoperations.jira.model.JiraChangeIssueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class JiraController { //TODO ДЛЯ ТЕСТОВ (ПОТОМ УДАЛИТЬ НА РЕЛИЗЕ)

    private final JiraClient client;
    private final JiraProperties properties;


    @GetMapping
    public ResponseEntity<?> test() {
        var issues = client.findSubIssues(properties.getFilterTaskKey()).getIssues();
        var issue = issues.get(0);
        var tr = client.findAvailableStatusesOfIssue(issue.getKey());
        var t = tr.getTransitions().stream().filter(transition ->
                transition.getTo().getId() == Integer.parseInt(JiraIssueStatus.IN_PROGRESS.getStatusId())
        ).toList().get(0);
        client.changeIssueStatus(issue.getKey(), new JiraChangeIssueStatus(t));
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }
}
