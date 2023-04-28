package com.nsu.midpointmassiveoperations.jira.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JiraIssueStatus {

    private int id;

    private String name;

    private JiraStatusCategory statusCategory;

}
