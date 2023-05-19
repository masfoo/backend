package com.nsu.midpointmassiveoperations.midpoint.operation;

import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.UserType;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteOperationTest {

    @Mock
    private MidpointClient client;

    @InjectMocks
    private DeleteOperation deleteOperation;

    @Test
    void MidpointDoesNotResponseWhenSearchUsers() {

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT));

        Ticket ticket = new Ticket();
        ticket.setTicketBody("some filters");
        OperationResultMessage result = deleteOperation.execute(ticket);

        verify(client, times(1)).searchUsers("some filters");
        assertEquals(OperationStatus.MIDPOINT_DOESNT_RESPONSE, result.status());

    }

    @Test
    void SearchUsersReturnedNull() {

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody("some filters");
        OperationResultMessage result = deleteOperation.execute(ticket);

        verify(client, times(1)).searchUsers("some filters");
        verify(client, never()).deleteUser(any());

        assertEquals(OperationStatus.FAILED, result.status());

    }

    @Test
    void SearchUsersReturnedSomeUsers() {

        ObjectListType usersFound = new ObjectListType();
        usersFound.setUserType(List.of(constructUserType("1"), constructUserType("2")));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(usersFound, HttpStatus.OK));
        when(client.deleteUser(any())).
                thenReturn(new ResponseEntity<>("ignore", HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody("some filters");
        OperationResultMessage result = deleteOperation.execute(ticket);

        verify(client, times(1)).searchUsers("some filters");
        verify(client, times(1)).deleteUser("1");
        verify(client, times(1)).deleteUser("2");

        assertEquals(OperationStatus.TO_JIRA, result.status());

    }

    @Test
    void MidpointDoesNotResponseWhenDeleteUsers() {

        ObjectListType usersFound = new ObjectListType();
        usersFound.setUserType(List.of(constructUserType("1"), constructUserType("2"), constructUserType("3")));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(usersFound, HttpStatus.OK));
        when(client.deleteUser(any())).
                thenReturn(new ResponseEntity<>("ignore", HttpStatus.OK)).
                thenReturn(new ResponseEntity<>("ignore", HttpStatus.GATEWAY_TIMEOUT));

        Ticket ticket = new Ticket();
        ticket.setTicketBody("some filters");
        OperationResultMessage result = deleteOperation.execute(ticket);

        verify(client, times(1)).searchUsers("some filters");
        verify(client, never()).deleteUser("3");
        assertEquals(OperationStatus.MIDPOINT_DOESNT_RESPONSE, result.status());

    }



    private UserType constructUserType(String oid){
        UserType user = new UserType();
        user.setOid(oid);
        return user;
    }
}
