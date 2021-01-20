package com.creations.funds.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import static com.creations.funds.constants.Constants.SERVICE_DATE_PATTERN;

public class SchemeData {

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SERVICE_DATE_PATTERN)
    private Date date;

    @JsonProperty("nav")
    private double nav;

    public SchemeData() {
    }

    public SchemeData(Date date, double nav) {
        this.date = date;
        this.nav = nav;
    }

    public Date getDate() {
        return date;
    }

    public double getNav() {
        return nav;
    }
}
