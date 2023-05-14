package com.nsu.midpointmassiveoperations.tickets.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class TicketBody {
    private String label;
    private String query;
}
