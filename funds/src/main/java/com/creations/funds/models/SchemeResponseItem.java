package com.creations.funds.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemeResponseItem {

    @JsonProperty("month")
    private String month;

    @JsonProperty("returns")
    private double returns;

    @JsonProperty("start_nav")
    private SchemeData startNav;

    @JsonProperty("end_nav")
    private SchemeData endNav;

    public SchemeResponseItem() {
    }

    public SchemeResponseItem(String month, double returns, SchemeData startNav, SchemeData endNav) {
        this.month = month;
        this.returns = returns;
        this.startNav = startNav;
        this.endNav = endNav;
    }

    public String getMonth() {
        return month;
    }

    public double getReturns() {
        return returns;
    }

    public SchemeData getStartNav() {
        return startNav;
    }

    public SchemeData getEndNav() {
        return endNav;
    }
}
