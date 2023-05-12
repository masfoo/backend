package com.nsu.midpointmassiveoperations.tickets;

public class TicketBodyParser {
    public static TicketData parse(String body) {
        boolean success = false;
        int start = 0;
        for(;start < body.length();++start){
            if (Character.isWhitespace(body.charAt(start))){
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
        return new TicketData(label, query);
    }
}
