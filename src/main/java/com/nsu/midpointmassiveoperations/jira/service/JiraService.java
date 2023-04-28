package com.nsu.midpointmassiveoperations.jira.service;

import com.nsu.midpointmassiveoperations.events.model.ChangeIssuesStatusEvent;
import com.nsu.midpointmassiveoperations.events.model.NewIssuesEvent;
import com.nsu.midpointmassiveoperations.jira.client.JiraClient;
import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.jira.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final JiraClient client;
    private final JiraProperties properties;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "${check-jira}")
    public void getTickets() {
        IssuesResult result = client.findSubIssues(properties.getFilterTaskKey());
        List<Issue> newIssues = result.getIssues().stream()
                .filter(issue ->
                        Objects.equals(issue.getFields().getStatus().getId(), JiraIssueStatus.NEW.getStatusId())
                )
                .collect(Collectors.toList());
        applicationEventPublisher.publishEvent(new NewIssuesEvent(newIssues));
        newIssues.forEach(
                issue -> {
                    JiraIssueAvailableStatuses availableStatusesOfIssue = client.findAvailableStatusesOfIssue(issue.getKey());
                    JiraIssueTransition issueTransition = availableStatusesOfIssue.getTransitions().stream().filter(transition ->
                            transition.getTo().getId() == Integer.parseInt(JiraIssueStatus.IN_PROGRESS.getStatusId())
                    ).toList().get(0); //для новых задач всегда есть переход к статусу "в работе"
                    client.changeIssueStatus(issue.getKey(), new JiraChangeIssueStatus(issueTransition));
                }
        );
    }

    @EventListener
    public void handleChangeIssuesStatusEvent(ChangeIssuesStatusEvent event) {
        event.getIssues().forEach(
                issue -> {
                    JiraIssueAvailableStatuses availableStatusesOfIssue = client.findAvailableStatusesOfIssue(issue.getKey());
                    JiraIssueTransition issueTransition = availableStatusesOfIssue.getTransitions().stream().filter(transition ->
                            transition.getTo().getId() == Integer.parseInt(event.getStatus().getStatusId())
                    ).toList().get(0); //TODO вот тут проверку надо добавить + вынести в приват метод смену статуса задачи
                    client.changeIssueStatus(issue.getKey(), new JiraChangeIssueStatus(issueTransition));
                }
        );
    }

}
