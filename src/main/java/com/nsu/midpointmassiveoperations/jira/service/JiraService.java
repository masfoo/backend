package com.nsu.midpointmassiveoperations.jira.service;

import com.nsu.midpointmassiveoperations.events.model.MidpointProcessedTicketsEvent;
import com.nsu.midpointmassiveoperations.events.model.NewTicketsEvent;
import com.nsu.midpointmassiveoperations.exception.JiraDoesntResponseException;
import com.nsu.midpointmassiveoperations.jira.client.JiraClient;
import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.jira.model.*;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.service.TicketService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JiraService {

    private final JiraClient client;
    private final JiraProperties properties;
    private final TicketService ticketService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    public void completeUnfinishedTickets() {
        applicationEventPublisher.publishEvent(
                new NewTicketsEvent(
                        ticketService.findAllByCurrentOperationStatus(OperationStatus.TO_MIDPOINT)
                )
        );
    }

    @Scheduled(cron = "${check-jira}")
    public void getTickets() {
        IssuesResult result = client.findSubIssues(properties.getFilterTaskKey());
        List<Issue> newIssues = selectIssues(result, JiraIssueStatus.NEW);
        changeStatuses
                (newIssues.stream().map(Issue::getKey).toList(),
                        JiraIssueStatus.IN_PROGRESS
                );
        applicationEventPublisher.publishEvent(new NewTicketsEvent(ticketService.saveNewTicketsFromJiraIssues(newIssues)));
    }

    @EventListener
    public void handleMidpointProcessedTicketsEvent(MidpointProcessedTicketsEvent event) {
        try {
            if (event.getTickets()!= null) {
                sendResult(event.getTickets());
            }
        } catch (JiraDoesntResponseException e) {
            event.getTickets().forEach(
                    ticket -> {
                        if (ticket.getCurrentOperationStatus() != OperationStatus.COMPLETED) {
                            ticket.setCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE);
                            ticketService.save(ticket);
                        }
                    }
            );
        }

    }

    @Scheduled(cron = "${retry}")
    public void resendTicketsResult() {
        try {
            List<Ticket> tickets = ticketService.findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE);
            tickets.forEach(ticket -> ticket.setCurrentOperationStatus(ticket.getPreviousOperationStatus()));
            sendResult(tickets);
        }
        catch (JiraDoesntResponseException e){
            log.info("Cannot resend results because Jira is still unavailable");
        }

    }

    public void sendResult(List<Ticket> tickets) {
        tickets.forEach(ticket -> {
            if (ticket.getCurrentOperationStatus() != OperationStatus.MIDPOINT_DOESNT_RESPONSE) {
                client.addCommentToIssue(ticket.getJiraTaskKey(), new JiraComment(ticket.getResult()));
                changeStatus(ticket.getJiraTaskKey(), JiraIssueStatus.COMPLETED);
                ticket.setCurrentOperationStatus(OperationStatus.COMPLETED);
                ticketService.save(ticket);
            }

        });
    }

    private List<Issue> selectIssues(IssuesResult result, JiraIssueStatus status) {
        return result.getIssues().stream()
                .filter(issue ->
                        Objects.equals(issue.getFields().getStatus().getId(), status.getStatusId())
                )
                .collect(Collectors.toList());
    }

    private Optional<JiraIssueTransition> getTransition(List<JiraIssueTransition> transitions, JiraIssueStatus status) {
        return transitions.stream().filter(transition ->
                        transition.getTo().getId() == Integer.parseInt(status.getStatusId())
                )
                .findFirst();
    }

    private void changeStatuses(List<String> issueKeys, JiraIssueStatus status) {
        issueKeys.forEach(
                key -> {
                    changeStatus(key, status);
                }
        );
    }

    public void changeStatus(String key, JiraIssueStatus status) {
        JiraIssueAvailableStatuses availableStatusesOfIssue = client.findAvailableStatusesOfIssue(key);
        Optional<JiraIssueTransition> issueTransition =
                getTransition(availableStatusesOfIssue.getTransitions(), status);
        issueTransition.ifPresent(jiraIssueTransition ->
                client.changeIssueStatus(key, new JiraChangeIssueStatus(jiraIssueTransition)));
    }

}
