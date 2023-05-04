package com.nsu.midpointmassiveoperations.midpoint.service;

import com.nsu.midpointmassiveoperations.events.model.ChangeIssuesStatusEvent;
import com.nsu.midpointmassiveoperations.events.model.NewIssuesEvent;
import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.model.Issue;
import com.nsu.midpointmassiveoperations.midpoint.service.operation.MidpointOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MidpointService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<String, MidpointOperation> operations;

    @Async("taskExecutor")
    @EventListener
    public void handleNewTickets(NewIssuesEvent event) {
        List<Issue> issueList = event.getIssues();
        issueList.forEach(issue -> {
            String operationName = issue.getFields().getSummary();
            String filter = (String) issue.getFields().getDescription();
            if (operations.containsKey(operationName)) {
                operations.get(operationName).execute(filter);
            }
        });
        applicationEventPublisher.publishEvent(new ChangeIssuesStatusEvent(event.getIssues(), JiraIssueStatus.COMPLETED));
    }
}
