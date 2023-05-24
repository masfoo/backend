package com.nsu.midpointmassiveoperations.midpoint.operation;

import com.nsu.midpointmassiveoperations.exception.MidpointDoesntResponseException;
import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(MidpointOperations.DELETE)
@Slf4j
public class DeleteOperation extends MidpointOperation {

    public DeleteOperation(MidpointClient client) {
        super(client);
    }

    @Override
    public OperationResultMessage execute(Ticket ticket) {
        String ticketBody = ticket.getTicketBody();
        ResponseEntity<ObjectListType> response = client.searchUsers(ticketBody.trim());
        if (response.getStatusCode().is5xxServerError()) {
            return new OperationResultMessage(OperationStatus.MIDPOINT_DOESNT_RESPONSE, "");//TODO здесь должено быть нормально сообщение
        }

        ObjectListType body = response.getBody();
        if (body == null) {
            log.error("body is null for query: " + ticketBody); //
            return new OperationResultMessage(OperationStatus.FAILED, ""); //TODO здесь должено быть нормально сообщение
        }
        List<UserType> users = body.getUserType();

        if (users == null) {
            return new OperationResultMessage(OperationStatus.TO_JIRA, "нет таких юзеров"); //TODO здесь должено быть нормально сообщение
        }

        try {
            users.forEach(userType -> {
                        ResponseEntity<String> deleteResponse = client.deleteUser(userType.getOid());
                        if (deleteResponse.getStatusCode().is5xxServerError()) {
                            throw new MidpointDoesntResponseException(""); //TODO здесь должено быть нормально сообщение
                        }
                    }
            );
        } catch (MidpointDoesntResponseException e) {
            return new OperationResultMessage(OperationStatus.MIDPOINT_DOESNT_RESPONSE, "");//TODO здесь должено быть нормально сообщение
        }
        return new OperationResultMessage(OperationStatus.TO_JIRA, ""); //TODO здесь должено быть нормально сообщение
    }
}
