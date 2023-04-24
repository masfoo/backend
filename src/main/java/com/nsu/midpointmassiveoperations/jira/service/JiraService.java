package com.nsu.midpointmassiveoperations.jira.service;

import com.nsu.midpointmassiveoperations.jira.client.JiraClient;
import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.jira.model.IssuesResult;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JiraService {

    private final JiraClient client;
    private final JiraProperties properties;

    @Scheduled(cron = "${check-jira}")
    public void getTickets() {
        IssuesResult result = client.findSubIssues(properties.getFilterTaskKey());
        result.getIssues().stream()
                .filter(issue ->
                        Objects.equals(issue.getFields().getStatus().getId(), JiraIssueStatus.NEW.getStatusId())
                )
                .collect(Collectors.toList());
        //TODO сделать ивент, чтобы сервер мидпоинта(его еще нет) получал тикеты как listener
    }

}
