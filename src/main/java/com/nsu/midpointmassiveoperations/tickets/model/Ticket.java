package com.nsu.midpointmassiveoperations.tickets.model;


import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operation;

    @Column(name = "ticket_body", columnDefinition = "text")
    private String ticketBody;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_operation_status")
    private OperationStatus currentOperationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_operation_status")
    private OperationStatus previousOperationStatus;

    private String result;

    private String jiraTaskKey;

    public void setId(Long id) {
        this.id = id;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setTicketBody(String ticketBody) {
        this.ticketBody = ticketBody;
    }

    public void setCurrentOperationStatus(OperationStatus currentOperationStatus) {
        setPreviousOperationStatus(getCurrentOperationStatus());
        this.currentOperationStatus = currentOperationStatus;
    }

    public void setPreviousOperationStatus(OperationStatus previousOperationStatus) {
        this.previousOperationStatus = previousOperationStatus;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setJiraTaskKey(String jiraTaskKey) {
        this.jiraTaskKey = jiraTaskKey;
    }
}
