package com.maghrebia.offer.mapper;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.dto.PurchasedOfferRequest;
import com.maghrebia.offer.dto.helpers.*;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.model.PurchasedOffer;
import com.maghrebia.offer.model.records.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class PurchasedOfferMapper {
//    public static PurchasedOfferResp toDto(PurchasedOffer entity) {
//        return new PurchasedOfferRequest(
//                entity.getFormId(),
//                entity.getOfferId(),
//                entity.getData().stream()
//                        .map(data -> PurchasedOfferDataDto.builder()
//                                .fieldLabel(data.fieldLabel())
//                                .fieldValue(data.fieldValue())
//                                .build())
//                        .collect(Collectors.toList())
//        );
//    }

    public static PurchasedOffer toEntity(PurchasedOfferRequest request) {
        return PurchasedOffer.builder()
                .formId(request.formId())
                .offerId(request.offerId())
                .data(request.data().stream()
                        .map(dto -> new PurchasedOfferData(dto.fieldLabel(), dto.fieldType(), dto.fieldValue()))
                        .collect(Collectors.toList()))
                .build();
    }
}
