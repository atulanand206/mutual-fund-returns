package com.creations.funds.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemeLite {

    @JsonProperty("scheme_code")
    private int schemeCode;

    @JsonProperty("scheme_name")
    private String schemeName;

    public int getSchemeCode() {
        return schemeCode;
    }

    public String getSchemeName() {
        return schemeName;
    }
}
