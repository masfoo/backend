package com.nsu.midpointmassiveoperations.tickets.utility;

import com.nsu.midpointmassiveoperations.jira.model.Issue;

public final class TicketHelper {

    public static String getOperation(Issue issue) {
        return issue.getFields().getSummary();
    }

    public static String getBody(Issue issue) {
        return (String) issue.getFields().getDescription();
    }
}
