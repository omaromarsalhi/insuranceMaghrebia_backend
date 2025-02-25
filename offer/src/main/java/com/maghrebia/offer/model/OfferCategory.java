package com.maghrebia.offer.model;

import com.maghrebia.offer.model.enums.CategoryTarget;
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
    private String categoryId;

    private String name;

    private String description;

    private String imageUri;

    private CategoryTarget categoryTarget;

    @CreatedDate
    private Date createdAt;

}
