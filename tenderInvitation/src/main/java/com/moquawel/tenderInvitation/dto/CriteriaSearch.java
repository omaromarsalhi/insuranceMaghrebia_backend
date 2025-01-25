package com.moquawel.tenderInvitation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;




@AllArgsConstructor
public class CriteriaSearch {
    private String key;
    private String value;
    private String specificSearch;

    public Map<String, String> toMap() {
        Map<String, String> criteriaMap = new HashMap<>();
        criteriaMap.put("key", this.key);
        criteriaMap.put("value", this.value);
        criteriaMap.put("specificSearch", this.specificSearch);
        return criteriaMap;
    }
}
