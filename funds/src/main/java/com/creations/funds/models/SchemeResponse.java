package com.creations.funds.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SchemeResponse {

    @JsonProperty("meta")
    private SchemeMeta schemeMeta;

    @JsonProperty("response_items")
    private List<SchemeResponseItem> responseItems;

    public SchemeResponse() {
        responseItems = new ArrayList<>();
    }

    public SchemeResponse(SchemeMeta schemeMeta, List<SchemeResponseItem> responseItems) {
        this.schemeMeta = schemeMeta;
        this.responseItems = responseItems;
    }

    public SchemeMeta getSchemeMeta() {
        return schemeMeta;
    }

    public List<SchemeResponseItem> getResponseItems() {
        return responseItems;
    }

}
