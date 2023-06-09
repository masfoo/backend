package com.nsu.midpointmassiveoperations.midpoint.operation;

import com.nsu.midpointmassiveoperations.exception.MidpointDoesntResponseException;
import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.nsu.midpointmassiveoperations.midpoint.operation.model.ResultMessageSupplier.*;
import static com.nsu.midpointmassiveoperations.midpoint.operation.model.ResultMessageSupplierAnswers.*;

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
            return midpointNoResponseOperation(MIDPOINT_REACH + response.getStatusCode());
        }

        ObjectListType body = response.getBody();
        if (body == null) {
            log.error("body is null for query: " + ticketBody); //
            return failedOperation(BODY_IS_NULL + ticketBody);
        }
        List<UserType> users = body.getUserType();

        if (users == null) {
            return jiraOperation(USER_NOT_FOUND);
        }

        try {
            users.forEach(userType -> {
                        ResponseEntity<String> deleteResponse = client.deleteUser(userType.getOid());
                        if (deleteResponse.getStatusCode().is5xxServerError()) {
                            throw new MidpointDoesntResponseException(deleteResponse.getStatusCode().toString());
                        }
                    }
            );
        } catch (MidpointDoesntResponseException e) {
            return midpointNoResponseOperation(MIDPOINT_REACH + e.getMessage());
        }
        return jiraOperation(SUCCESS);
    }
}
