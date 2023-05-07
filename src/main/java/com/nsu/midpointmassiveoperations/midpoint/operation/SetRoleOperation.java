package com.nsu.midpointmassiveoperations.midpoint.operation;


import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointOperations;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.RoleListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(MidpointOperations.SET_ROLE)
@RequiredArgsConstructor
@Slf4j
public class SetRoleOperation implements MidpointOperation {

    private final MidpointClient client;

    @Override
    public void execute(String ticketBody) { //TODO сделать проверки

        int indexOfNewLine = ticketBody.indexOf("\n");//TODO парсер написать как отдельный класс
        String role = ticketBody.substring(0, indexOfNewLine).trim();
        String filterForUsers = ticketBody.substring(indexOfNewLine).trim();

        ResponseEntity<ObjectListType> bodyResponse = client.searchUsers(filterForUsers);
        ResponseEntity<RoleListType> roleResponse = client.searchRole(role);
        ObjectListType objectBody = bodyResponse.getBody();
        RoleListType roleBody = roleResponse.getBody();
        if (objectBody == null || roleBody == null) {
            log.error("body is null for ticket: " + ticketBody);
            return;
        }
        List<UserType> users = objectBody.getUserType();
        users.forEach(userType ->
                client.setUserRole(userType.getOid(), roleBody.getRoleType().getOid())
        );
    }
}
