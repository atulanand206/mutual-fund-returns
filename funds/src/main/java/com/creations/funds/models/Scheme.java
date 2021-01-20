package com.creations.funds.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Scheme {

    @JsonProperty("meta")
    private SchemeMeta meta;

    @JsonProperty("data")
    private List<SchemeData> data;

    @JsonProperty("status")
    private String status;

    public SchemeMeta getMeta() {
        return meta;
    }

    public List<SchemeData> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }
}
