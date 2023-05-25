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
public class SetResourceOperationTests {

    @Mock
    private MidpointClient client;

    @InjectMocks
    private SetResourceOperation setResourceOperation;



    @Test
    void MidpointDoesNotResponseWhenSearchUsers() {

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new resource", "some filters"));
        OperationResultMessage result = setResourceOperation.execute(ticket);

        assertEquals(OperationStatus.MIDPOINT_DOESNT_RESPONSE, result.status());

    }

    @Test
    void MidpointDoesNotResponseWhenSearchRole() {

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(client.searchResources("new resource")).
                thenReturn(new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new resource", "some filters"));
        OperationResultMessage result = setResourceOperation.execute(ticket);

        assertEquals(OperationStatus.MIDPOINT_DOESNT_RESPONSE, result.status());

    }

    @Test
    void BothSearchesWereSuccessful() {

        ObjectListType usersFound = new ObjectListType();
        usersFound.setUserType(List.of(constructUserType("1"), constructUserType("2")));

        ResourceListType resourceFound = new ResourceListType();
        resourceFound.setResourceType(constructResourceType("resource1"));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(usersFound, HttpStatus.OK));
        when(client.searchResources("new resource")).
                thenReturn(new ResponseEntity<>(resourceFound, HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new resource", "some filters"));
        OperationResultMessage result = setResourceOperation.execute(ticket);

        verify(client, times(1)).setResourceToUser("1", "resource1");
        verify(client, times(1)).setResourceToUser("2", "resource1");

        assertEquals(OperationStatus.TO_JIRA, result.status());

    }

    @Test
    void ResourceSearchWasUnsuccessful() {

        ObjectListType usersFound = new ObjectListType();
        usersFound.setUserType(List.of(constructUserType("1"), constructUserType("2")));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(usersFound, HttpStatus.OK));
        when(client.searchResources("new resource")).
                thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new resource", "some filters"));
        OperationResultMessage result = setResourceOperation.execute(ticket);

        verify(client, never()).setResourceToUser(any(),any());

        assertEquals(OperationStatus.FAILED, result.status());

    }

    @Test
    void UsersSearchWasUnsuccessful() {


        ResourceListType resourceFound = new ResourceListType();
        resourceFound.setResourceType(constructResourceType("resource1"));

        when(client.searchUsers("some filters")).
                thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
        when(client.searchResources("new resource")).
                thenReturn(new ResponseEntity<>(resourceFound, HttpStatus.OK));

        Ticket ticket = new Ticket();
        ticket.setTicketBody(createValidTicketBody("new resource", "some filters"));
        OperationResultMessage result = setResourceOperation.execute(ticket);

        verify(client, never()).setResourceToUser(any(),any());

        assertEquals(OperationStatus.FAILED, result.status());

    }

    @Test
    void EmptyTicketBodyShouldFail() {

        Ticket ticket = new Ticket();
        ticket.setTicketBody("\n\n\n");
        OperationResultMessage result = setResourceOperation.execute(ticket);

        verify(client, never()).setResourceToUser(any(),any());

        verify(client, never()).searchResources(any());
        verify(client, never()).searchUsers(any());

        assertEquals(OperationStatus.FAILED, result.status());

    }

    private String createValidTicketBody(String resource, String query){
        return resource + "\n" + query;
    }

    private UserType constructUserType(String oid){
        UserType user = new UserType();
        user.setOid(oid);
        return user;
    }

    private ResourceType constructResourceType(String oid){
        ResourceType resource = new ResourceType();
        resource.setOid(oid);
        return resource;
    }

}
