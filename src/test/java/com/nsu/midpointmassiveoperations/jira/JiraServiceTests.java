package com.nsu.midpointmassiveoperations.jira;

import com.nsu.midpointmassiveoperations.events.model.MidpointProcessedTicketsEvent;
import com.nsu.midpointmassiveoperations.events.model.NewTicketsEvent;
import com.nsu.midpointmassiveoperations.exception.JiraDoesntResponseException;
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

import java.util.*;

import static org.mockito.AdditionalMatchers.or;
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
        jiraService = spy(new JiraService(jiraClient, properties, ticketService, eventPublisher));

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
    void sendResultJiraDoesntResponse() {
        List<Ticket> ticketsToResend = Arrays.asList(
                constructTicket(OperationStatus.TO_JIRA, null, "k", "c"),
                constructTicket(OperationStatus.TO_JIRA, null, "k", "c"));

        doNothing().doThrow(new JiraDoesntResponseException("test")).when(jiraClient).addCommentToIssue(any(), any());
        when(jiraClient.findAvailableStatusesOfIssue(any())).
                thenReturn(constructAvailableStatus(JiraIssueStatus.COMPLETED));

        jiraService.handleMidpointProcessedTicketsEvent(new MidpointProcessedTicketsEvent(ticketsToResend));

        verify(jiraService, times(1)).changeStatus(any(), any());
        verify(jiraService, times(1)).sendResult(any());
        verify(ticketService, times(1)).save(argThat(a -> a.getCurrentOperationStatus() == OperationStatus.COMPLETED));
        verify(ticketService, times(1)).save(argThat(a -> a.getCurrentOperationStatus() == OperationStatus.JIRA_DOESNT_RESPONSE));
    }

    @Test
    void sendResultNull() {
        jiraService.handleMidpointProcessedTicketsEvent(new MidpointProcessedTicketsEvent(null));

        verify(jiraService, never()).sendResult(any());
    }

    @Test
    void sendResultOk() {
        List<Ticket> ticketsToResend = Arrays.asList(
                constructTicket(OperationStatus.MIDPOINT_DOESNT_RESPONSE, null, "k1", "c1"),
                constructTicket(OperationStatus.TO_JIRA, null, "k2", "c2"));

        when(jiraClient.findAvailableStatusesOfIssue(any())).
                thenReturn(constructAvailableStatus(JiraIssueStatus.COMPLETED));

        jiraService.handleMidpointProcessedTicketsEvent(new MidpointProcessedTicketsEvent(ticketsToResend));

        verify(jiraService, times(1)).changeStatus(any(),any());
        verify(jiraService, times(1)).sendResult(ticketsToResend);
    }

    @Test
    void noTicketsFoundResend() {

        when(ticketService.findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE)).
                thenReturn(new LinkedList<>());

        jiraService.resendTicketsResult();

        verify(ticketService, times(1)).findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE);

        verify(jiraClient, never()).addCommentToIssue(any(),any());
    }

    @Test
    void someTicketsFoundResend() {
        List<Ticket> ticketsToResend = Arrays.asList(
                constructTicket(OperationStatus.JIRA_DOESNT_RESPONSE, OperationStatus.TO_JIRA, "k1", "c1"),
                constructTicket(OperationStatus.JIRA_DOESNT_RESPONSE, OperationStatus.TO_JIRA, "k2", "c2"));

        when(ticketService.findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE)).
                thenReturn(ticketsToResend);
        when(jiraClient.findAvailableStatusesOfIssue(any())).
                thenReturn(constructAvailableStatus(JiraIssueStatus.COMPLETED));


        jiraService.resendTicketsResult();

        verify(ticketService, times(1)).findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE);

        verify(jiraClient, times(1)).addCommentToIssue(eq("k1"), argThat(a -> a.getBody()=="c1"));
        verify(jiraClient, times(1)).addCommentToIssue(eq("k2"), argThat(a -> a.getBody()=="c2"));
    }

    @Test
    void JiraDoesntResponseExceptionWhenResend() {
        List<Ticket> ticketsToResend = Arrays.asList(
                constructTicket(OperationStatus.JIRA_DOESNT_RESPONSE, OperationStatus.TO_JIRA, "k1", "c1"),
                constructTicket(OperationStatus.JIRA_DOESNT_RESPONSE, OperationStatus.TO_JIRA, "k2", "c2"));

        when(ticketService.findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE)).
                thenReturn(ticketsToResend);
        doThrow(new JiraDoesntResponseException("test")).when(jiraClient).addCommentToIssue(any(),any());


        jiraService.resendTicketsResult();

        verify(ticketService, times(1)).findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE);

        verify(jiraClient, times(1)).addCommentToIssue(any(),any());
        verify(jiraService, never()).changeStatus(any(),any());
        verify(ticketService, never()).save(any());
    }

    @Test
    void JiraDoesntResponseExceptionDelayedWhenResend() {
        List<Ticket> ticketsToResend = Arrays.asList(
                constructTicket(OperationStatus.JIRA_DOESNT_RESPONSE, OperationStatus.TO_JIRA, "k", "c"),
                constructTicket(OperationStatus.JIRA_DOESNT_RESPONSE, OperationStatus.TO_JIRA, "k", "c"));

        when(ticketService.findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE)).
                thenReturn(ticketsToResend);
        doNothing().doThrow(new JiraDoesntResponseException("test")).when(jiraClient).addCommentToIssue(any(),any());
        when(jiraClient.findAvailableStatusesOfIssue(any())).
                thenReturn(constructAvailableStatus(JiraIssueStatus.COMPLETED));

        jiraService.resendTicketsResult();

        verify(ticketService, times(1)).findAllByCurrentOperationStatus(OperationStatus.JIRA_DOESNT_RESPONSE);

        verify(jiraClient, times(2)).addCommentToIssue(eq("k"), argThat(a -> a.getBody() == "c"));
        verify(jiraService, times(1)).changeStatus("k",JiraIssueStatus.COMPLETED);
        verify(ticketService, times(1)).save(any());
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

    private Ticket constructTicket(OperationStatus status, OperationStatus previousStatus, String key, String comment){
        Ticket ticket = new Ticket();
        ticket.setCurrentOperationStatus(status);
        ticket.setPreviousOperationStatus(previousStatus);
        ticket.setJiraTaskKey(key);
        ticket.setResult(comment);
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
