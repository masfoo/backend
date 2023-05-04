package com.nsu.midpointmassiveoperations.midpoint.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonRootName(value = "object")
@JacksonXmlRootElement(localName = "object", namespace = "http://prism.evolveum.com/xml/ns/public/types-3")
@Getter
@Setter
public class ObjectListType {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "object", namespace = "http://midpoint.evolveum.com/xml/ns/public/common/api-types-3")
    private List<UserType> userType;
}