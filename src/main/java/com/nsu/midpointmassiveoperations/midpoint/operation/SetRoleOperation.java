package com.nsu.midpointmassiveoperations.midpoint.operation;


import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.RoleListType;
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

import static com.nsu.midpointmassiveoperations.midpoint.operation.model.ResultMessageSupplier.*;
import static com.nsu.midpointmassiveoperations.midpoint.operation.model.ResultMessageSupplierAnswers.*;

@Component(MidpointOperations.SET_ROLE)
@Slf4j
public class SetRoleOperation extends MidpointOperation {

    public SetRoleOperation(MidpointClient client) {
        super(client);
    }

    @Override
    public OperationResultMessage execute(Ticket ticket) { //TODO сделать проверки

        String ticketBody = ticket.getTicketBody();
        TicketBody ticketData = TicketBodyParser.parse(ticketBody);
        if (ticketData == null) {
            log.error("empty ticket");
            return failedOperation(EMPTY_TICKET);

        }

        ResponseEntity<ObjectListType> bodyResponse = client.searchUsers(ticketData.getQuery());
        if (bodyResponse.getStatusCode().is5xxServerError()) {
            return midpointNoResponseOperation(MIDPOINT_REACH + bodyResponse.getStatusCode());
        }

        ResponseEntity<RoleListType> roleResponse = client.searchRole(ticketData.getLabel());
        if (roleResponse.getStatusCode().is5xxServerError()) {
            return midpointNoResponseOperation(MIDPOINT_REACH + roleResponse.getStatusCode());
        }

        ObjectListType objectBody = bodyResponse.getBody();
        RoleListType roleBody = roleResponse.getBody();
        if (objectBody == null || roleBody == null) {
            log.error("body is null for ticket: " + ticketBody);
            return failedOperation(BODY_IS_NULL);
        }
        List<UserType> users = objectBody.getUserType();
        users.forEach(userType ->
                client.setUserRole(userType.getOid(), roleBody.getRoleType().getOid())
        );
        return jiraOperation(SUCCESS);

    }
}
