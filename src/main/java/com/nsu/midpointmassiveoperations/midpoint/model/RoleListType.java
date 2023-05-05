package com.nsu.midpointmassiveoperations.midpoint.model;


import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

@JsonRootName(value = "object")
@JacksonXmlRootElement(localName = "object", namespace = "http://prism.evolveum.com/xml/ns/public/types-3")
@Getter
@Setter
public class RoleListType {

    @JacksonXmlProperty(localName = "object", namespace = "http://midpoint.evolveum.com/xml/ns/public/common/api-types-3")
    private RoleType roleType;
}
