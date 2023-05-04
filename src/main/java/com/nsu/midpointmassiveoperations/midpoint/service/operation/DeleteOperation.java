package com.nsu.midpointmassiveoperations.midpoint.service.operation;

import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(MidpointOperations.DELETE)
@RequiredArgsConstructor
@Slf4j
public class DeleteOperation implements MidpointOperation {

    private final MidpointClient client;

    @Override
    public void execute(String xmlFilter) {
        ResponseEntity<ObjectListType> response = client.searchUsers(xmlFilter);
        ObjectListType body = response.getBody();
        if (body == null) {
            log.error("body is null for query: " + xmlFilter);
            return;
        }
        List<UserType> users = body.getUserType();
        users.forEach(userType ->
                client.deleteUser(userType.getOid())
        );
    }
}
