package com.maghrebia.offer.model;


import com.maghrebia.offer.model.records.BenefitType;
import com.maghrebia.offer.model.records.FilteredCategory;
import com.maghrebia.offer.model.records.OfferLabel;
import com.maghrebia.offer.model.records.OfferPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;


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

    @Field("category")
    private FilteredCategory category;

    private List<OfferLabel> labels;

    private List<BenefitType> benefits;

    private List<String> tags;

    private List<OfferPackage> packages;

    private String formId;

    @CreatedDate
    private LocalDateTime createdAt;


}