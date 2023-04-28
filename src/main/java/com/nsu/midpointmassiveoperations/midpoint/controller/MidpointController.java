package com.nsu.midpointmassiveoperations.midpoint.controller;

import com.evolveum.midpoint.xml.ns._public.common.api_types_3.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.client.MidpointClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test1")
@RequiredArgsConstructor
public class MidpointController { //TODO ДЛЯ ТЕСТОВ (ПОТОМ УДАЛИТЬ НА РЕЛИЗЕ)

    private final MidpointClient client;


    @GetMapping
    public ResponseEntity<?> test() {
        String xml = "<query>\n" +
                "\n" +
                "  <filter>\n" +
                "\n" +
                "    <or>\n" +
                "\n" +
                "      <substring>\n" +
                "\n" +
                "        <matching>polyStringNorm</matching>     \t<!-- normalized (case insensitive) -->\n" +
                "\n" +
                "        <path>givenName</path>\n" +
                "\n" +
                "        <value>a</value>\n" +
                "\n" +
                "        <anchorStart>true</anchorStart>         \t<!-- should start with a given string -->\n" +
                "\n" +
                "      </substring>\n" +
                "\n" +
                "      <substring>\n" +
                "\n" +
                "        <matching>polyStringNorm</matching>     \t<!-- normalized (case insensitive) -->\n" +
                "\n" +
                "        <path>givenName</path>\n" +
                "\n" +
                "        <value>b</value>\n" +
                "\n" +
                "        <anchorStart>true</anchorStart>         \t<!-- should start with a given string -->\n" +
                "\n" +
                "      </substring>\n" +
                "\n" +
                "    </or>\n" +
                "\n" +
                "  </filter>\n" +
                "\n" +
                "</query>";

        ResponseEntity<ObjectListType> response = client.searchUsers(xml);
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}
