package com.nsu.midpointmassiveoperations.tickets.service;

import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.model.Fields;
import com.nsu.midpointmassiveoperations.jira.model.Issue;
import com.nsu.midpointmassiveoperations.jira.model.Status;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTests {

    @Mock
    private TicketRepository ticketRepository;

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService(ticketRepository);
    }

    @Test
    void saveNewTicketsFromJiraIssueTest() {
        List<Issue> issues = List.of(constructIssue(),
                constructIssue());

        ticketService.saveNewTicketsFromJiraIssues(issues);

        verify(ticketRepository, times(1)).saveAll(argThat(a->
            ((List<Ticket>)a).stream().allMatch(t-> t.getCurrentOperationStatus().equals(OperationStatus.TO_MIDPOINT))));
    }

    Issue constructIssue(){
        Status jiraStatus = new Status();
        jiraStatus.setId(JiraIssueStatus.NEW.getStatusId());
        Fields fields = new Fields();
        fields.setStatus(jiraStatus);
        Issue issue = new Issue();
        issue.setFields(fields);
        return issue;
    }
}
