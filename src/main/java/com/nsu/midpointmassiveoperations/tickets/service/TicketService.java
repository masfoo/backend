package com.nsu.midpointmassiveoperations.tickets.service;

import com.nsu.midpointmassiveoperations.jira.model.Issue;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.repository.TicketRepository;
import com.nsu.midpointmassiveoperations.tickets.utility.TicketHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Ticket> findAllByCurrentOperationStatus(OperationStatus status) {
        return ticketRepository.findAllByCurrentOperationStatus(status);
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> saveNewTicketsFromJiraIssues(List<Issue> issueList) {
        List<Ticket> tickets = issueList.stream()
                .map(issue ->
                        {
                            Ticket ticket = new Ticket();
                            ticket.setTicketBody(TicketHelper.getBody(issue));
                            ticket.setOperation(TicketHelper.getOperation(issue));
                            ticket.setCurrentOperationStatus(OperationStatus.TO_MIDPOINT);
                            ticket.setJiraTaskKey(issue.getKey());
                            return ticket;
                        }
                )
                .toList();
        ticketRepository.saveAll(tickets);
        return findAllByCurrentOperationStatus(OperationStatus.TO_MIDPOINT);
    }

}
