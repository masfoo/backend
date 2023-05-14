package com.nsu.midpointmassiveoperations.midpoint.operation.model;

import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;

public record OperationResultMessage(OperationStatus status, String result) {

}
