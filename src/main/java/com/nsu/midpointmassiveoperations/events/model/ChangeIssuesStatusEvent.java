package com.nsu.midpointmassiveoperations.events.model;

import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.model.Issue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChangeIssuesStatusEvent {

    private List<Issue> issues;

    private JiraIssueStatus status;
}
