package com.nsu.midpointmassiveoperations.midpoint.operation;


import com.nsu.midpointmassiveoperations.exception.MidpointDoesntResponseException;
import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.ResourceListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.ResultMessageSupplier;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.model.TicketBody;
import com.nsu.midpointmassiveoperations.tickets.utility.TicketBodyParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.nsu.midpointmassiveoperations.midpoint.operation.model.ResultMessageSupplierAnswers.*;

@Component(MidpointOperations.SET_RESOURCE)
@Slf4j
public class SetResourceOperation extends MidpointOperation {

    public SetResourceOperation(MidpointClient client) {
        super(client);
    }

    @Override
    public OperationResultMessage execute(Ticket ticket) { //TODO сделать проверки

        String ticketBody = ticket.getTicketBody();
        TicketBody ticketData = TicketBodyParser.parse(ticketBody);
        if (ticketData == null) {
            log.error("empty ticket");
            return ResultMessageSupplier.failedOperation(EMPTY_TICKET);

        }
        ResponseEntity<ObjectListType> bodyResponse = client.searchUsers(ticketData.getQuery());

        if (bodyResponse.getStatusCode().is5xxServerError()) {
            return ResultMessageSupplier.midpointNoResponseOperation(MIDPOINT_REACH + bodyResponse.getStatusCode());
        }

        ResponseEntity<ResourceListType> resourceResponse = client.searchResources(ticketData.getLabel());
        if (resourceResponse.getStatusCode().is5xxServerError()) {
            return ResultMessageSupplier.midpointNoResponseOperation(MIDPOINT_REACH + resourceResponse.getStatusCode());
        }
        ObjectListType objectBody = bodyResponse.getBody();
        ResourceListType resourceBody = resourceResponse.getBody();
        if (objectBody == null || resourceBody == null) {
            log.error("body is null for ticket: " + ticketBody);
            return ResultMessageSupplier.failedOperation(BODY_IS_NULL);

        }
        List<UserType> users = objectBody.getUserType();
        try {
            users.forEach(userType ->{
                        ResponseEntity<String> setResponse =client.setResourceToUser(userType.getOid(), resourceBody.getResourceType().getOid());
                        if (setResponse.getStatusCode().is5xxServerError()) {
                            throw new MidpointDoesntResponseException(setResponse.getStatusCode().toString());
                        }
                    }
            );
        }catch (MidpointDoesntResponseException e) {
            return ResultMessageSupplier.midpointNoResponseOperation(MIDPOINT_REACH + e.getMessage());
        }
        return ResultMessageSupplier.jiraOperation(SUCCESS);

    }
}
