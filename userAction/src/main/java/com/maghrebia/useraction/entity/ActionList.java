package com.maghrebia.useraction.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionList {
    @JsonProperty("description")
    private String description;
    @JsonProperty("time")
    private String time;
}
