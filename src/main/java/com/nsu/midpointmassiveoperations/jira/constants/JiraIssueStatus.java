package com.nsu.midpointmassiveoperations.jira.constants;

import lombok.Getter;

@Getter
public enum JiraIssueStatus {

    NEW("10000"),
    IN_PROGRESS("10001"),
    COMPLETED("10002");

    private final String statusId;

    JiraIssueStatus(String statusId) {
        this.statusId = statusId;
    }
}
