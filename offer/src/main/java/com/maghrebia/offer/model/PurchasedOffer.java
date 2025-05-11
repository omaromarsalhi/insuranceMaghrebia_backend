package com.maghrebia.offer.model;


import com.maghrebia.offer.model.records.PurchasedOfferData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "purchased_offer")
public class PurchasedOffer {

    @Id
    private String purchasedOfferId;

    private String formId;

    private String offerId;

    private String userId;

    private List<PurchasedOfferData> data;
}
