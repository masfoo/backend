package com.nsu.midpointmassiveoperations.midpoint.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserType {
    @JacksonXmlProperty(isAttribute = true)
    private String oid;
    @JacksonXmlProperty(isAttribute = true)
    private String version;
    @JacksonXmlProperty(localName = "name", namespace = "http://midpoint.evolveum.com/xml/ns/public/common/common-3")
    private String name;
    @JacksonXmlProperty
    private int iteration;
    @JacksonXmlProperty
    private String iterationToken;
    @JacksonXmlProperty
    private String givenName;

}
