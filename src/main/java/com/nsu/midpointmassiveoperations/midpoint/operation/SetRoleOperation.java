package com.nsu.midpointmassiveoperations.midpoint.operation;


import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.RoleListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.tickets.utility.TicketBodyParser;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import com.nsu.midpointmassiveoperations.tickets.model.TicketBody;
import com.nsu.midpointmassiveoperations.tickets.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

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
            return new OperationResultMessage(OperationStatus.FAILED, "");//TODO здесь должено быть нормально сообщение

        }

        ResponseEntity<ObjectListType> bodyResponse = client.searchUsers(ticketData.getQuery());
        if (bodyResponse.getStatusCode().is5xxServerError()) {
            return new OperationResultMessage(OperationStatus.MIDPOINT_DOESNT_RESPONSE, "");//TODO здесь должено быть нормально сообщение
        }

        ResponseEntity<RoleListType> roleResponse = client.searchRole(ticketData.getLabel());
        ObjectListType objectBody = bodyResponse.getBody();
        RoleListType roleBody = roleResponse.getBody();
        if (objectBody == null || roleBody == null) {
            log.error("body is null for ticket: " + ticketBody);
            return new OperationResultMessage(OperationStatus.FAILED, "");//TODO здесь должено быть нормально сообщение
        }
        List<UserType> users = objectBody.getUserType();
        users.forEach(userType ->
                client.setUserRole(userType.getOid(), roleBody.getRoleType().getOid())
        );
        return new OperationResultMessage(OperationStatus.TO_JIRA, "" ); //TODO здесь должено быть нормально сообщение

    }
}
