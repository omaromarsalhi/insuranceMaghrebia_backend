package com.maghrebia.offer.dto;


import com.maghrebia.offer.dto.helpers.PurchasedOfferDataDto;
import com.maghrebia.offer.model.records.PurchasedOfferData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public record PurchasedOfferRequest(

        String formId,

        String offerId,

        String userId,

        List<PurchasedOfferDataDto> data
) {
}
