package com.nsu.midpointmassiveoperations.events.model;

import com.nsu.midpointmassiveoperations.jira.model.Issue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NewIssuesEvent {

    private List<Issue> issues;

}
