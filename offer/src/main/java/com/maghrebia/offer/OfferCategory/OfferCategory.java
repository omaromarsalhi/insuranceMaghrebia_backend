package com.maghrebia.offer.OfferCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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

    private String imageUri;

    @CreatedDate  // This will be populated automatically by Spring MongoDB Auditing
    private Date createdAt;

}
