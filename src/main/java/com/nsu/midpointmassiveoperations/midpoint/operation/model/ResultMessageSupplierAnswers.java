package com.nsu.midpointmassiveoperations.midpoint.operation.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResultMessageSupplierAnswers {
    public static final String MIDPOINT_REACH = "Couldn't reach midpoint. ";
    public static final String SUCCESS = "Operation successful. ";
    public static final String USER_NOT_FOUND = "User not found. ";
    public static final String BODY_IS_NULL = "Body is null for query. ";
    public static final String EMPTY_TICKET = "Cannot parse an empty ticket. ";
}
