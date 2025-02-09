package com.maghrebia.offer.offer;


import com.maghrebia.offer.OfferCategory.OfferCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "offer")
public class Offer {

    @Id
    private String offerId;
    private String name;
    private String description;
    private double premium;
    private LocalDateTime validity;
    private boolean isActive;

    @DBRef
    private OfferCategory offerCategory;

}