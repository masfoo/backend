package com.nsu.midpointmassiveoperations.midpoint.operation;


import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.ResourceListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import com.nsu.midpointmassiveoperations.tickets.TicketBodyParser;
import com.nsu.midpointmassiveoperations.tickets.model.TicketBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(MidpointOperations.SET_RESOURCE)
@RequiredArgsConstructor
@Slf4j
public class SetResourceOperation implements MidpointOperation {

    private final MidpointClient client;

    @Override
    public void execute(String ticketBody) { //TODO сделать проверки

        TicketBody ticketData = TicketBodyParser.parse(ticketBody);
        if (ticketData == null){
            log.error("empty ticket");
            return;
        }
        ResponseEntity<ObjectListType> bodyResponse = client.searchUsers(ticketData.getQuery());
        ResponseEntity<ResourceListType> resourceResponse = client.searchResources(ticketData.getLabel());
        ObjectListType objectBody = bodyResponse.getBody();
        ResourceListType resourceBody = resourceResponse.getBody();
        if (objectBody == null || resourceBody == null) {
            log.error("body is null for ticket: " + ticketBody);
            return;
        }
        List<UserType> users = objectBody.getUserType();
        users.forEach(userType ->
                client.setResourceToUser(userType.getOid(), resourceBody.getResourceType().getOid())
        );
    }
}
