package com.nsu.midpointmassiveoperations.jira.controller;

import com.nsu.midpointmassiveoperations.jira.constants.JiraProperties;
import com.nsu.midpointmassiveoperations.jira.client.JiraClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class JiraController { //TODO ДЛЯ ТЕСТОВ (ПОТОМ УДАЛИТЬ НА РЕЛИЗЕ)

    private final JiraClient client;
    private final JiraProperties properties;


    @GetMapping
    public ResponseEntity<?> test() {
        return new ResponseEntity<>(client.findSubIssues(properties.getFilterTaskKey()), HttpStatus.OK);
    }
}
