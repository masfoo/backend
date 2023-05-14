package com.nsu.midpointmassiveoperations.events.model;

import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MidpointProcessedTicketsEvent {

    private List<Ticket> tickets;
}
