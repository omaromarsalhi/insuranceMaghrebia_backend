package com.maghrebia.useraction.entity;

import lombok.*;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ReportResponse {

    private String userAnalysis;
    private String classification;
    private Map<String, List<ActionStrategy>> actions;
}
