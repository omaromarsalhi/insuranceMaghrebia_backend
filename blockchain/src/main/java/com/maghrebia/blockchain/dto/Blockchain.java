package com.maghrebia.blockchain.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Blockchain implements Serializable{

    @JsonProperty("paymentId")
    private int paymentId;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("fullname")
    private String fullname;

    @Override
    public String toString() {
        return "Blockchain{" +
                "id='" + paymentId + '\'' +
                ", amount='" + amount + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", fullname='" + fullname + '\'' +
                '}';
    }

}
