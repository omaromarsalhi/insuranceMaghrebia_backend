package com.moquawel.tenderInvitation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moquawel.tenderInvitation.dto.CriteriaSearch;
import com.moquawel.tenderInvitation.feignclient.*;
import com.moquawel.tenderInvitation.offer.Offer;
import com.moquawel.tenderInvitation.offer.OfferRepository;
import com.moquawel.tenderInvitation.request.FilterRequest;
import com.moquawel.tenderInvitation.response.PayloadResponse;
import com.moquawel.tenderInvitation.response.OfferResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
@Slf4j
public class TenderInvitationService {

    @Value("${search-service.pbkStrId}")
    private int pbkStrId;
    private final RetrieveClient retrieveClient;
    private final AoAndGeneralInfoClient aoAndGeneralInfoClient;
    private final LotAndArticleInfo lotAndArticleInfo;
    private final ProdInfo prodInfo;
    private final AgrementInfo agrementInfo;
    private final TermsOfReferenceInfo termsOfReferenceInfo;
    private final OfferRepository offerRepository;
    private final OfferService offerService;
    private final ObjectMapper objectMapper;


    public Object getTermsOfReferenceInfo(String bidNo) {
        return getInfo(() -> termsOfReferenceInfo.getTermsOfReferenceInfoClient(bidNo), "the terms of reference info");
    }

    public Object getAgrementInfo(String bidNo) {
        return getInfo(() -> agrementInfo.getAgrementInfoClient(bidNo), "the agreement info");
    }

    public Object getProdInfo(String bidNo) {
        return getInfo(() -> prodInfo.getProdInfoClient(bidNo), "the product info");
    }

    public Object getLotAndArticleInfo(String bidNo) {
        return getInfo(() -> lotAndArticleInfo.getLotAndArticleInfoClient(bidNo), "the lot and article info");
    }

    public Object getAoAndGeneralInfo(String epBidMasterId) {
        return getInfo(() -> aoAndGeneralInfoClient.getAoAndGeneralInfoClient(epBidMasterId), "the AO and general info");
    }

    private Object getInfo(Supplier<ResponseEntity<?>> clientCall, String logMessage) {
        try {
            return clientCall.get().getBody();
        } catch (Exception e) {
            log.error("Error occurred while retrieving {}: {}", logMessage, e.getMessage());
            return null;
        }
    }


    public PayloadResponse getFilteredOffers(FilterRequest request) {
        List<Offer> response;

        try {
            response = offerService.findFilteredData(request);
        } catch (Exception e) {
            log.error("this error occurred while retrieving the offers: {}", e.getMessage());
            response = new ArrayList<>();
        }

        return PayloadResponse
                .builder()
                .total(response.size())
                .data(response)
                .build();
    }


    public void getOffersFromTuneps() {

        Map<String, Object> formData = new HashMap<>();
        var currentDateTime = this.formatNowDate();

        formData.put("publicYn", 'Y');
        formData.put("pbkStrId", pbkStrId);
        formData.put("publicDt", currentDateTime);

        List<Map<String, String>> searchPayload = generateSearchCriteria(formData);

        Map<String, Object> payload = new HashMap<>();
        payload.put("dataSearch", searchPayload);

        ResponseEntity<OfferResponse> response = retrieveClient.retrieveClient(payload);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            var newOffers = this.getOnlyNewOffers(response.getBody().payload(), currentDateTime);
            this.addCategoryToOffers(newOffers);
            offerRepository.saveAll(newOffers);
        } else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private List<Offer> getOnlyNewOffers(PayloadResponse payload, String currentDateTime) {
        return payload
                .data()
                .stream()
                .filter(data -> compareDateTime(currentDateTime, data.getPublicDt()))
                .peek(data -> {
                    data.setMyBdRecvEndDt(this.fromStringToDatetime(data.getBdRecvEndDt()));
                    data.setBidModSeq(null);
                    data.setMyPublicDt(this.fromStringToDatetime(data.getPublicDt()));
                    data.setPublicDt(null);
                })
                .toList();
    }

    private void addCategoryToOffers(List<Offer> offers) {
        offers.forEach(offer -> {
            String category = getCategoryFromResponse(offer.getEpBidMasterId());

            Integer formattedBudget = getFormattedBudgetFromResponse(offer.getBidNo());

            offer.setCategory(category);
            offer.setCompanyType((formattedBudget != null && formattedBudget > 10000) ? "A" : "B");
        });
    }

    private String getCategoryFromResponse(long epBidMasterId) {
        try {
            ResponseEntity<?> categoryResponse = aoAndGeneralInfoClient.getAoAndGeneralInfoClient(String.valueOf(epBidMasterId));
            JsonNode rootNode = objectMapper.convertValue(categoryResponse.getBody(), JsonNode.class);
            return rootNode.path("payload").path("bizKindStrFr").asText().trim();
        } catch (Exception e) {
            log.error("Failed to fetch category for id: {}: {}", epBidMasterId, e.getMessage());
            return null;
        }
    }

    private Integer getFormattedBudgetFromResponse(String bidNo) {
        try {
            ResponseEntity<?> budgetResponse = lotAndArticleInfo.getLotAndArticleInfoClient(bidNo);
            JsonNode rootNode = objectMapper.convertValue(budgetResponse.getBody(), JsonNode.class);
            String budget = rootNode.path("payload").get(0).path("guaranteeAmountCurrF").asText();
            return Integer.parseInt(budget.trim().split(" ")[0]);
        } catch (Exception e) {
            log.error("Failed to fetch category for bidNo: {}: {}", bidNo, e.getMessage());
            return null;
        }
    }


    private List<Map<String, String>> generateSearchCriteria(Map<String, Object> formData) {
        List<Map<String, String>> options = new ArrayList<>();

        if (formData.containsKey("publicDt")) {
            String formattedPublicDt = (String) formData.get("publicDt");
            options.add(new CriteriaSearch("publicDt", formattedPublicDt, ">=").toMap());
        }

        if (formData.containsKey("publicDt_end")) {
            String formattedPublicDtEnd = (String) formData.get("publicDt_end");
            options.add(new CriteriaSearch("publicDt", formattedPublicDtEnd, "<=").toMap());
        }

        if (formData.containsKey("pbkStrId")) {
            options.add(new CriteriaSearch("pbkStrId", formData.get("pbkStrId").toString(), "equals").toMap());
        }

        if (formData.containsKey("publicYn")) {
            options.add(new CriteriaSearch("publicYn", formData.get("publicYn").toString(), "=").toMap());
        }

        if (formData.containsKey("bidNo")) {
            options.add(new CriteriaSearch("bidNo", formData.get("bidNo").toString().trim(), "upper_like").toMap());
        }

        return options;
    }


    private LocalDateTime fromStringToDatetime(String datetime1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(datetime1.substring(0, datetime1.indexOf('.')), formatter);
    }

    private boolean compareDateTime(String datetime1, String datetime2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse(datetime1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(datetime2.substring(0, datetime2.indexOf('.')), formatter);
        return dateTime1.isBefore(dateTime2);
    }

    private String formatNowDate() {
        LocalDate date = LocalDate.now();
//        LocalDate date = LocalDate.of(2024,9,27);
//        LocalDateTime dateTime = date.atTime(LocalDateTime.now().getHour(), 0, 0);
        LocalDateTime dateTime = date.atTime(11, 0, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }


}
