package com.nsu.midpointmassiveoperations.midpoint.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleType {
    @JacksonXmlProperty(localName = "oid", isAttribute = true)
    private String oid;
}
