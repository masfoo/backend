package com.nsu.midpointmassiveoperations.midpoint.client;


import com.nsu.midpointmassiveoperations.midpoint.constants.MidpointProperties;
import com.nsu.midpointmassiveoperations.midpoint.constants.Templates;
import com.nsu.midpointmassiveoperations.midpoint.model.ObjectListType;
import com.nsu.midpointmassiveoperations.midpoint.model.ResourceListType;
import com.nsu.midpointmassiveoperations.midpoint.model.RoleListType;
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

        HttpHeaders headers = createHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<>(xmlQuery, headers);
        return restTemplate.exchange(
                midpointProperties.getBaseUrl() + "/users/search",
                HttpMethod.POST,
                entity,
                ObjectListType.class);
    }

    public ResponseEntity<RoleListType> searchRole(String roleName) {

        String xmlQuery = String.format(Templates.SEARCH_QUERY, roleName);
        HttpHeaders headers = createHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<>(xmlQuery, headers);
        return restTemplate.exchange(
                midpointProperties.getBaseUrl() + "/roles/search",
                HttpMethod.POST,
                entity,
                RoleListType.class);
    }

    public ResponseEntity<ResourceListType> searchResources(String resourceName) {

        String xmlQuery = String.format(Templates.SEARCH_QUERY, resourceName);
        HttpHeaders headers = createHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        HttpEntity<String> entity = new HttpEntity<>(xmlQuery, headers);
        return restTemplate.exchange(
                midpointProperties.getBaseUrl() + "/resources/search",
                HttpMethod.POST,
                entity,
                ResourceListType.class);
    }

    public ResponseEntity<String> deleteUser(String oid) {
        String deleteUserUrl = midpointProperties.getBaseUrl() + "/users/" + oid;
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        return restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, entity, String.class);
    }

    public ResponseEntity<String> setUserRole(String userOid, String roleOid) {
        String blockUserUrl = midpointProperties.getBaseUrl() + "/users/" + userOid;
        String blockUserRequestBody = String.format("<objectModification\n" +
                "    xmlns='http://midpoint.evolveum.com/xml/ns/public/common/api-types-3'\n" +
                "    xmlns:c='http://midpoint.evolveum.com/xml/ns/public/common/common-3'\n" +
                "    xmlns:t=\"http://prism.evolveum.com/xml/ns/public/types-3\">\n" +
                "    <itemDelta>\n" +
                "        <t:modificationType>add</t:modificationType>\n" +
                "        <t:path>c:assignment</t:path>\n" +
                "        <t:value>\n" +
                "                <c:targetRef oid=\"%s\" type=\"c:RoleType\" />\n" +
                "        </t:value>\n" +
                "    </itemDelta>\n" +
                "</objectModification>", roleOid);

        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(blockUserRequestBody, headers);

        return restTemplate.exchange(blockUserUrl, HttpMethod.POST, entity, String.class);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setBasicAuth(midpointProperties.getLogin(), midpointProperties.getPassword());
        return headers;
    }
}