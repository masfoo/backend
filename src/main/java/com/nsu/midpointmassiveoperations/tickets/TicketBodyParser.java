package com.nsu.midpointmassiveoperations.tickets;

import com.nsu.midpointmassiveoperations.tickets.model.TicketBody;

public class TicketBodyParser {
    public static TicketBody parse(String body) {
        boolean success = false;
        int start = 0;
        for(;start < body.length();++start){
            if (!Character.isWhitespace(body.charAt(start))){
                success = true;
                break;
            }
        }
        if (!success) {
            return null;
        }
        int nlIndex = body.indexOf('\n', start);
        String label = body.substring(start, nlIndex).trim();
        String query = body.substring(nlIndex).trim();
        return new TicketBody(label, query);
    }
}
