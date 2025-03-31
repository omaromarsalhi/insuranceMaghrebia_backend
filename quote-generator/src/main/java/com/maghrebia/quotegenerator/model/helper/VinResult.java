package com.maghrebia.quotegenerator.model.helper;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VinResult {
    @JsonProperty("Value")
    private String value;

    @JsonProperty("ValueId")
    private String valueId;

    @JsonProperty("Variable")
    private String variable;

    @JsonProperty("VariableId")
    private Integer variableId;

}