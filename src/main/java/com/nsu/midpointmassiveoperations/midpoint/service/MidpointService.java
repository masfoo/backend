package com.nsu.midpointmassiveoperations.midpoint.service;

import com.nsu.midpointmassiveoperations.events.model.MidpointProcessedTicketsEvent;
import com.nsu.midpointmassiveoperations.events.model.NewTicketsEvent;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.midpoint.operation.MidpointOperation;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MidpointService {

    private final TicketService ticketService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Map<String, MidpointOperation> operations;

    @Async("taskExecutor")
    @EventListener
    public void handleNewTickets(NewTicketsEvent event) {

        List<Ticket> tickets = event.getTickets();
        if (tickets == null) {
            return;
        }
        tickets.forEach(this::handleTicket);
        applicationEventPublisher.publishEvent(new MidpointProcessedTicketsEvent(tickets));
    }

    @Scheduled(cron = "${retry}")
    public void retryProcessTickets() {
        List<Ticket> tickets = ticketService.findAllByCurrentOperationStatus(OperationStatus.MIDPOINT_DOESNT_RESPONSE);
        tickets.forEach(this::handleTicket);
        applicationEventPublisher.publishEvent(new MidpointProcessedTicketsEvent(tickets));
    }

    public void handleTicket(Ticket ticket) {
        String operationName = ticket.getOperation();
        if (operations.containsKey(operationName)) {
            OperationResultMessage message = operations.get(operationName).execute(ticket);
            ticket.setCurrentOperationStatus(message.status());
            ticket.setResult(message.result());
            ticketService.save(ticket);
        }
        else{
            ticket.setCurrentOperationStatus(OperationStatus.FAILED);
            ticket.setResult("Unknown operation");
            ticketService.save(ticket);
        }
    }
}
