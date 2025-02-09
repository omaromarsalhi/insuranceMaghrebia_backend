package com.maghrebia.offer.OfferCategory;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "offer_category")
public class OfferCategory {
    @Id
    private String offerCategoryId;

    private String name;

    private String description;

}