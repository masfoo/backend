package com.nsu.midpointmassiveoperations.jira;

import com.nsu.midpointmassiveoperations.events.model.NewTicketsEvent;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import org.mockito.ArgumentMatcher;

import java.util.List;
import java.util.stream.IntStream;

public class NewTicketsEventMatcher implements ArgumentMatcher<NewTicketsEvent> {
    private final List<Ticket> expectedTickets;

    public NewTicketsEventMatcher(List<Ticket> expectedTickets) {
        this.expectedTickets = expectedTickets;
    }

    @Override
    public boolean matches(NewTicketsEvent actualEvent) {
        if (actualEvent == null) {
            return false;
        }

        List<Ticket> actualTickets = actualEvent.getTickets();
        return (expectedTickets == null && actualTickets == null) ||
                expectedTickets != null && actualTickets != null && actualTickets.size() == expectedTickets.size() &&
                IntStream.range(0,actualTickets.size()).allMatch(i->expectedTickets.get(i).getCurrentOperationStatus().
                        equals(actualTickets.get(i).getCurrentOperationStatus()));
    }

}