package com.nsu.midpointmassiveoperations.tickets;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketData {
    private String label;
    private String query;


    public TicketData(String label, String query) {
        this.label = label;
        this.query = query;
    }
}
