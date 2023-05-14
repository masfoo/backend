package com.nsu.midpointmassiveoperations.events.model;

import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NewTicketsEvent {

    private List<Ticket> tickets;

}
