package com.nsu.midpointmassiveoperations.midpoint.constants;

public final class Templates {

    public static final String SEARCH_QUERY= "<query>\n" +
            "    <filter>\n" +
            "        <substring>\n" +
            "            <matching>polyStringNorm</matching> <!-- normalized (case insensitive) -->\n" +
            "            <path>name</path>\n" +
            "            <value>%s</value>\n" +
            "            <anchorStart>true</anchorStart> <!-- should start with a given string -->\n" +
            "        </substring>\n" +
            "    </filter>\n" +
            "</query>";
}
