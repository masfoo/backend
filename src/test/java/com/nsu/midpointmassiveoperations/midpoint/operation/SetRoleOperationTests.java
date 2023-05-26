package com.nsu.midpointmassiveoperations.midpoint.operation;

import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import com.nsu.midpointmassiveoperations.midpoint.model.*;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetRoleOperationTests {
    @Mock
    private MidpointClient client;

    @InjectMocks
    private SetRoleOperation setRoleOperation;



    @Test
    void MidpointDoesNotResponseWhenSearchUsers() {

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new role", "some filters"));
        OperationResultMessage result = setRoleOperation.execute(ticket);

        assertEquals(OperationStatus.MIDPOINT_DOESNT_RESPONSE, result.status());

    }

    @Test
    void MidpointDoesNotResponseWhenSearchRole() {

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(client.searchRole("new role")).
                thenReturn(new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new role", "some filters"));
        OperationResultMessage result = setRoleOperation.execute(ticket);

        assertEquals(OperationStatus.MIDPOINT_DOESNT_RESPONSE, result.status());

    }

    @Test
    void BothSearchesWereSuccessful() {

        ObjectListType usersFound = new ObjectListType();
        usersFound.setUserType(List.of(constructUserType("1"), constructUserType("2")));

        RoleListType roleFound = new RoleListType();
        roleFound.setRoleType(constructRoleType("role1"));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(usersFound, HttpStatus.OK));
        when(client.searchRole("new role")).
                thenReturn(new ResponseEntity<>(roleFound, HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new role", "some filters"));
        OperationResultMessage result = setRoleOperation.execute(ticket);

        verify(client, times(1)).setUserRole("1", "role1");
        verify(client, times(1)).setUserRole("2", "role1");

        assertEquals(OperationStatus.TO_JIRA, result.status());

    }

    @Test
    void RoleSearchWasUnsuccessful() {

        ObjectListType usersFound = new ObjectListType();
        usersFound.setUserType(List.of(constructUserType("1"), constructUserType("2")));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(usersFound, HttpStatus.OK));
        when(client.searchRole("new role")).
                thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new role", "some filters"));
        OperationResultMessage result = setRoleOperation.execute(ticket);

        verify(client, never()).setUserRole(any(),any());

        assertEquals(OperationStatus.FAILED, result.status());

    }

    @Test
    void UsersSearchWasUnsuccessful() {

        RoleListType roleFound = new RoleListType();
        roleFound.setRoleType(constructRoleType("role1"));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        when(client.searchRole("new role")).
                thenReturn(new ResponseEntity<>(roleFound, HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new role", "some filters"));
        OperationResultMessage result = setRoleOperation.execute(ticket);

        verify(client, never()).setUserRole(any(),any());

        assertEquals(OperationStatus.FAILED, result.status());

    }

    @Test
    void EmptyTicketBodyShouldFail() {

        Ticket ticket = new Ticket();
        ticket.setTicketBody("\n\n\n");
        OperationResultMessage result = setRoleOperation.execute(ticket);

        verify(client, never()).setUserRole(any(),any());

        verify(client, never()).searchRole(any());
        verify(client, never()).searchUsers(any());

        assertEquals(OperationStatus.FAILED, result.status());

    }

    private String createValidTicketBody(String role, String query){
        return role + "\n" + query;
    }

    private UserType constructUserType(String oid){
        UserType user = new UserType();
        user.setOid(oid);
        return user;
    }

    private RoleType constructRoleType(String oid){
        RoleType role = new RoleType();
        role.setOid(oid);
        return role;
    }

}
