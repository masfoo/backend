package com.nsu.midpointmassiveoperations.jira;

import com.nsu.midpointmassiveoperations.events.model.NewTicketsEvent;
import com.nsu.midpointmassiveoperations.jira.client.JiraClient;
import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.jira.model.*;
import com.nsu.midpointmassiveoperations.jira.service.JiraService;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JiraServiceTests {

    @Mock
    private JiraClient jiraClient;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private TicketService ticketService;

    private JiraService jiraService;

    @BeforeEach
    void setUp() {
        JiraProperties properties = new JiraProperties();
        properties.setFilterTaskKey("some filters");
        jiraService = new JiraService(jiraClient, properties, ticketService, eventPublisher);

    }



    @Test
    void noTicketsFoundPostConstruct() {
        when(ticketService.findAllByCurrentOperationStatus(any())).thenReturn(new ArrayList<>());

        jiraService.completeUnfinishedTickets();

        verify(ticketService, times(1)).findAllByCurrentOperationStatus(OperationStatus.TO_MIDPOINT);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void toMidpointTicketFoundPostConstruct() {
        List<Ticket> toMidpointTickets = Collections.singletonList(constructTicket(OperationStatus.TO_MIDPOINT));

        when(ticketService.findAllByCurrentOperationStatus(OperationStatus.TO_MIDPOINT)).thenReturn(toMidpointTickets);

        jiraService.completeUnfinishedTickets();

        verify(ticketService, times(1)).findAllByCurrentOperationStatus(OperationStatus.TO_MIDPOINT);
        verify(eventPublisher, times(1)).publishEvent(eq(new NewTicketsEvent(toMidpointTickets)));
    }



    @Test
    void noIssuesFoundScheduled() {
        IssuesResult jiraQueryResult = new IssuesResult();
        jiraQueryResult.setIssues(new ArrayList<>());
        when(jiraClient.findSubIssues("some filters")).thenReturn(jiraQueryResult);

        jiraService.getTickets();

        verify(jiraClient, times(1)).findSubIssues("some filters");
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void newIssueFoundScheduled() {
        List<Issue> newIssues = Collections.singletonList(constructIssue(JiraIssueStatus.NEW));

        IssuesResult jiraQueryResult = constructResult(newIssues);

        when(jiraClient.findSubIssues("some filters")).
                thenReturn(jiraQueryResult);
        when(jiraClient.findAvailableStatusesOfIssue(any())).
                thenReturn(constructAvailableStatus(JiraIssueStatus.IN_PROGRESS));
        List<Ticket> tickets = Collections.singletonList(constructTicket(OperationStatus.TO_MIDPOINT));
        when(ticketService.saveNewTicketsFromJiraIssues(newIssues)).
                thenReturn(tickets);

        jiraService.getTickets();

        verify(jiraClient, times(1)).findSubIssues("some filters");
        verify(jiraClient, times(1)).changeIssueStatus(any(), any());
        verify(eventPublisher, times(1)).publishEvent(argThat(new NewTicketsEventMatcher(tickets)));
    }

    @Test
    void newIssuesFoundScheduled() {
        List<Issue> newIssues = Arrays.asList(constructIssue(JiraIssueStatus.NEW), constructIssue(JiraIssueStatus.NEW));

        IssuesResult jiraQueryResult = constructResult(newIssues);

        when(jiraClient.findSubIssues("some filters")).
                thenReturn(jiraQueryResult);
        when(jiraClient.findAvailableStatusesOfIssue(any())).
                thenReturn(constructAvailableStatus(JiraIssueStatus.IN_PROGRESS));
        List<Ticket> tickets = Arrays.asList(
                constructTicket(OperationStatus.TO_MIDPOINT), constructTicket(OperationStatus.TO_MIDPOINT)
        );
        when(ticketService.saveNewTicketsFromJiraIssues(newIssues)).
                thenReturn(tickets);

        jiraService.getTickets();

        verify(jiraClient, times(1)).findSubIssues("some filters");
        verify(jiraClient, times(2)).changeIssueStatus(any(), any());
        verify(eventPublisher, times(1)).publishEvent(argThat(new NewTicketsEventMatcher(tickets)));
    }

    @Test
    void allKindsOfIssuesFoundScheduled() {
        Issue newIssue = constructIssue(JiraIssueStatus.NEW);
        Issue inProgressIssue = constructIssue(JiraIssueStatus.IN_PROGRESS);
        Issue completedIssue = constructIssue(JiraIssueStatus.COMPLETED);
        IssuesResult differentIssues = constructResult(Arrays.asList(inProgressIssue, completedIssue, newIssue));

        when(jiraClient.findSubIssues("some filters")).thenReturn(differentIssues);
        when(jiraClient.findAvailableStatusesOfIssue(any())).thenReturn(constructAvailableStatus(JiraIssueStatus.IN_PROGRESS));
        when(ticketService.saveNewTicketsFromJiraIssues(Collections.singletonList(newIssue))).thenReturn(Collections.singletonList(constructTicket(OperationStatus.TO_MIDPOINT)));
        jiraService.getTickets();

        verify(jiraClient, times(1)).findSubIssues("some filters");
        verify(jiraClient, times(1)).changeIssueStatus(any(), any());
        verify(eventPublisher, times(1)).publishEvent(argThat(new NewTicketsEventMatcher(Collections.singletonList(constructTicket(OperationStatus.TO_MIDPOINT)))));
    }
    @Test
    void noAvailableStatusFoundScheduled() {
        List<Issue> newIssues = Collections.singletonList(constructIssue(JiraIssueStatus.NEW));

        IssuesResult jiraQueryResult = constructResult(newIssues);

        when(jiraClient.findSubIssues("some filters")).
                thenReturn(jiraQueryResult);
        when(jiraClient.findAvailableStatusesOfIssue(any())).
                thenReturn(constructEmptyAvailableStatus());
        List<Ticket> tickets = Collections.singletonList(constructTicket(OperationStatus.TO_MIDPOINT));
        when(ticketService.saveNewTicketsFromJiraIssues(newIssues)).
                thenReturn(tickets);

        jiraService.getTickets();

        verify(jiraClient, times(1)).findSubIssues("some filters");
        verify(jiraClient, never()).changeIssueStatus(any(), any());
        verify(eventPublisher, times(1)).publishEvent(argThat(new NewTicketsEventMatcher(tickets)));
    }


    private Issue constructIssue(JiraIssueStatus status){
        Status jiraStatus = new Status();
        jiraStatus.setId(status.getStatusId());
        Fields fields = new Fields();
        fields.setStatus(jiraStatus);
        Issue issue = new Issue();
        issue.setFields(fields);
        return issue;
    }

    private IssuesResult constructResult(List<Issue> issues){
        IssuesResult result = new IssuesResult();
        result.setIssues(issues);
        return result;
    }

    private Ticket constructTicket(OperationStatus status){
        Ticket ticket = new Ticket();
        ticket.setCurrentOperationStatus(status);
        return ticket;
    }

    private JiraIssueAvailableStatuses constructAvailableStatus(JiraIssueStatus status){
        JiraIssueAvailableStatuses statuses = new JiraIssueAvailableStatuses();
        JiraIssueTransition transition = new JiraIssueTransition();
        com.nsu.midpointmassiveoperations.jira.model.JiraIssueStatus jiraStatus = new com.nsu.midpointmassiveoperations.jira.model.JiraIssueStatus();
        jiraStatus.setId(Integer.parseInt(status.getStatusId()));
        transition.setTo(jiraStatus);
        statuses.setTransitions(Collections.singletonList(transition));
        return statuses;
    }

    private JiraIssueAvailableStatuses constructEmptyAvailableStatus(){
        JiraIssueAvailableStatuses statuses = new JiraIssueAvailableStatuses();
        statuses.setTransitions(new ArrayList<>());
        return statuses;
    }


}
