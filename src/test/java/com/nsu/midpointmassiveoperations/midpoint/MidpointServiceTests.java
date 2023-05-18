package com.nsu.midpointmassiveoperations.midpoint;

import com.nsu.midpointmassiveoperations.events.model.MidpointProcessedTicketsEvent;
import com.nsu.midpointmassiveoperations.events.model.NewTicketsEvent;
import com.nsu.midpointmassiveoperations.jira.client.JiraClient;
import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.jira.service.JiraService;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.midpoint.operation.MidpointOperation;
import com.nsu.midpointmassiveoperations.midpoint.service.MidpointService;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MidpointServiceTests {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private TicketService ticketService;

    @Mock
    private Map<String, MidpointOperation> operations;

    private MidpointService midpointService;

    @BeforeEach
    void setUp() {
        midpointService = spy(new MidpointService(ticketService,applicationEventPublisher,operations));
    }



    @Test
    public void handleNewTicketsWithNullTicketList() {

        midpointService.handleNewTickets(new NewTicketsEvent(null));

        verify(midpointService, never()).handleTicket(any());

    }

    @Test
    public void handleNewTicketsWithEmptyTicketList() {

        midpointService.handleNewTickets(new NewTicketsEvent(new ArrayList<>()));

        verify(midpointService, never()).handleTicket(any());
        verify(applicationEventPublisher, never()).publishEvent(any());

    }

    @Test
    public void handleNewTicketsWithNotEmptyTicketList() {

        doNothing().when(midpointService).handleTicket(any());
        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);

        midpointService.handleNewTickets(new NewTicketsEvent(Arrays.asList(ticket1, ticket2)));

        verify(midpointService, times(1)).handleTicket(ticket1);
        verify(midpointService, times(1)).handleTicket(ticket2);

        verify(applicationEventPublisher, times(1)).publishEvent(eq(new MidpointProcessedTicketsEvent((Arrays.asList(ticket1, ticket2)))));

    }

    @Test
    public void retryProcessTicketsNoTicketsFound() {

        when(ticketService.findAllByCurrentOperationStatus(OperationStatus.MIDPOINT_DOESNT_RESPONSE)).
                thenReturn(new ArrayList<>());
        midpointService.retryProcessTickets();

        verify(midpointService, never()).handleTicket(any());

    }

    @Test
    public void retryProcessTicketsSomeTicketsFound() {

        doNothing().when(midpointService).handleTicket(any());

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        when(ticketService.findAllByCurrentOperationStatus(OperationStatus.MIDPOINT_DOESNT_RESPONSE)).
                thenReturn(Arrays.asList(ticket1, ticket2));

        midpointService.retryProcessTickets();

        verify(midpointService, times(1)).handleTicket(ticket1);
        verify(midpointService, times(1)).handleTicket(ticket2);
        verify(applicationEventPublisher, times(1)).publishEvent(eq(new MidpointProcessedTicketsEvent((Arrays.asList(ticket1, ticket2)))));

    }

}
