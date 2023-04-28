package com.nsu.midpointmassiveoperations.midpoint.service;

import com.nsu.midpointmassiveoperations.events.model.ChangeIssuesStatusEvent;
import com.nsu.midpointmassiveoperations.events.model.NewIssuesEvent;
import com.nsu.midpointmassiveoperations.jira.constants.JiraIssueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MidpointService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Async("taskExecutor")
    @EventListener
    public void handleNewIssueEvent(NewIssuesEvent event) throws InterruptedException {
        System.out.println(event);
        Thread.sleep(5000); //some work
        applicationEventPublisher.publishEvent(new ChangeIssuesStatusEvent(event.getIssues(), JiraIssueStatus.COMPLETED));
    }
}
