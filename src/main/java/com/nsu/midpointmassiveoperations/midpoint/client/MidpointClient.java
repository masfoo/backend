package com.nsu.midpointmassiveoperations.midpoint.client;


import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointProperties;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class MidpointClient {

    private final MidpointProperties midpointProperties;
    private final RestTemplate restTemplate;

    public ResponseEntity<ObjectListType> searchUsers(String xmlQuery) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        headers.setBasicAuth(midpointProperties.getLogin(), midpointProperties.getPassword());
        HttpEntity<String> entity = new HttpEntity<>(xmlQuery, headers);
        return restTemplate.exchange(
                "http://localhost:8080/midpoint/ws/rest/users/search",
                HttpMethod.POST,
                entity,
                ObjectListType.class);
    }

    public ResponseEntity<String> deleteUser(String oid) {
        String deleteUserUrl = "http://localhost:8080/midpoint/ws/rest/users/" + oid;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setBasicAuth(midpointProperties.getLogin(), midpointProperties.getPassword());
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        return restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, entity, String.class);
    }
}