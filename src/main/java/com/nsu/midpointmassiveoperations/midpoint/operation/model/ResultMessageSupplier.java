package com.nsu.midpointmassiveoperations.midpoint.operation.model;

import com.nsu.midpointmassiveoperations.midpoint.constants.OperationStatus;

public final class ResultMessageSupplier {
    public static OperationResultMessage failedOperation(String message){
        return new OperationResultMessage(OperationStatus.FAILED, message);
    }

    public static OperationResultMessage midpointNoResponseOperation(String message){
        return new OperationResultMessage(OperationStatus.MIDPOINT_DOESNT_RESPONSE, message);
    }

    public static OperationResultMessage jiraNoResponseOperation(String message){
        return new OperationResultMessage(OperationStatus.JIRA_DOESNT_RESPONSE, message);
    }

    public static OperationResultMessage completedOperation(String message){
        return new OperationResultMessage(OperationStatus.COMPLETED, message);
    }

    public static OperationResultMessage jiraOperation(String message){
        return new OperationResultMessage(OperationStatus.TO_JIRA, message);
    }

    public static OperationResultMessage midpointOperation(String message){
        return new OperationResultMessage(OperationStatus.TO_MIDPOINT, message);
    }
}

