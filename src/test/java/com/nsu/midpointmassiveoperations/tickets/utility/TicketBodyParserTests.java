package com.nsu.midpointmassiveoperations.tickets.utility;

import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.tickets.model.TicketBody;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TicketBodyParserTests {


    @Test
    public void parseEmptyShouldReturnNull(){
        TicketBody result = TicketBodyParser.parse("");
        assertNull(result);

    }

    @Test
    public void parseNoQuery(){
        TicketBody result = TicketBodyParser.parse("  \n \n role 1 \n \n  ");
        assertEquals("role 1", result.getLabel());
        assertEquals("", result.getQuery());
    }

    @Test
    public void parseSimpleBody(){
        TicketBody result = TicketBodyParser.parse("role\nquery");
        assertEquals("role", result.getLabel());
        assertEquals("query", result.getQuery());
    }

    @Test
    public void parseSpacedBody(){
        TicketBody result = TicketBodyParser.parse("role 1\nsome query");
        assertEquals("role 1", result.getLabel());
        assertEquals("some query", result.getQuery());
    }

    @Test
    public void parseNLTrimSpacedBody(){
        TicketBody result = TicketBodyParser.parse("\n\n\nrole 1\n\n\nsome query\n\n\n");
        assertEquals("role 1", result.getLabel());
        assertEquals("some query", result.getQuery());
    }

    @Test
    public void parseSpacesTrimSpacedBody(){
        TicketBody result = TicketBodyParser.parse("   role 1\nsome query      ");
        assertEquals("role 1", result.getLabel());
        assertEquals("some query", result.getQuery());
    }

    @Test
    public void parseSpacesAndNLTrimSpacedBody(){
        TicketBody result = TicketBodyParser.parse("  \n \n role 1 \n \n some query   \n \n   ");
        assertEquals("role 1", result.getLabel());
        assertEquals("some query", result.getQuery());
    }

}
