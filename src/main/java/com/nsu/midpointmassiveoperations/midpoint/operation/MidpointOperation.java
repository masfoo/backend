package com.nsu.midpointmassiveoperations.midpoint.operation;

import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import com.nsu.midpointmassiveoperations.midpoint.operation.model.OperationResultMessage;
import com.nsu.midpointmassiveoperations.tickets.model.Ticket;


public abstract class MidpointOperation {

    protected final MidpointClient client;

    public MidpointOperation(MidpointClient client) {
        this.client = client;
    }

    abstract public OperationResultMessage execute(Ticket ticket);
}
