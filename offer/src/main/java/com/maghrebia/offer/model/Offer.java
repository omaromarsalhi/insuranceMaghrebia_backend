package com.maghrebia.offer.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "offer")
public class Offer {

    @Id
    private String offerId;

    private String name;

    private String header;

    private String imageUri;

    private boolean isActive;

    private FilteredCategory category;

    private List<OfferLabel> labels;


    @CreatedDate
    private LocalDateTime createdAt;


}