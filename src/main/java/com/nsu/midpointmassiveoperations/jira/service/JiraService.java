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

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final JiraClient client;
    private final JiraProperties properties;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CountDownLatch latch = new CountDownLatch(1);

    @PostConstruct
    public void init() {
        IssuesResult result = client.findSubIssues(properties.getFilterTaskKey());
        List<Issue> newIssues = selectIssues(result,JiraIssueStatus.IN_PROGRESS);
        applicationEventPublisher.publishEvent(new NewIssuesEvent(newIssues));
        latch.countDown();
    }

    @Scheduled(cron = "${check-jira}")
    public void getTickets() throws InterruptedException {
        latch.await();
        IssuesResult result = client.findSubIssues(properties.getFilterTaskKey());
        List<Issue> newIssues = selectIssues(result,JiraIssueStatus.NEW);
        changeStatuses(newIssues, JiraIssueStatus.IN_PROGRESS);
        applicationEventPublisher.publishEvent(new NewIssuesEvent(newIssues));
    }

    @EventListener
    public void handleChangeIssuesStatusEvent(ChangeIssuesStatusEvent event) {
        changeStatuses(event.getIssues(), event.getStatus());
    }

    private List<Issue> selectIssues(IssuesResult result, JiraIssueStatus status){
        return result.getIssues().stream()
                .filter(issue ->
                        Objects.equals(issue.getFields().getStatus().getId(), status.getStatusId())
                )
                .collect(Collectors.toList());
    }

    private Optional<JiraIssueTransition> getTransition(List<JiraIssueTransition> transitions, JiraIssueStatus status){
        return transitions.stream().filter(transition ->
                        transition.getTo().getId() == Integer.parseInt(status.getStatusId())
                )
                .findFirst();
    }

    private void changeStatuses(List<Issue> issues, JiraIssueStatus status){
        issues.forEach(
                issue -> {
                    JiraIssueAvailableStatuses availableStatusesOfIssue = client.findAvailableStatusesOfIssue(issue.getKey());
                    Optional<JiraIssueTransition> issueTransition =
                            getTransition(availableStatusesOfIssue.getTransitions(), status);
                    issueTransition.ifPresent(jiraIssueTransition ->
                            client.changeIssueStatus(issue.getKey(), new JiraChangeIssueStatus(jiraIssueTransition)));
                }
        );
    }

}
