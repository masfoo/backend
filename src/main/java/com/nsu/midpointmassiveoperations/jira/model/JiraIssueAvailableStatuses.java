package com.nsu.midpointmassiveoperations.jira.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JiraIssueAvailableStatuses {
    private List<JiraIssueTransition> transitions;

}
