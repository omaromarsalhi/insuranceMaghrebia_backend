package com.maghrebia.offer.service;


import com.maghrebia.offer.dto.PurchasedOfferRequest;
import com.maghrebia.offer.mapper.PurchasedOfferMapper;
import com.maghrebia.offer.model.PurchasedOffer;
import com.maghrebia.offer.repository.OfferFormRepository;
import com.maghrebia.offer.repository.PurchasedOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@AllArgsConstructor
@Service
public class PurchasedOfferService {

    private final PurchasedOfferRepository purchasedOfferRepository;
    private final OfferFormRepository offerFormRepository;


    public PurchasedOffer create(PurchasedOfferRequest request) {
        var testResult = verifyPurchasedOfferData(request);
        if (testResult) {
            return purchasedOfferRepository.save(PurchasedOfferMapper.toEntity(request));
        }
        throw new RuntimeException("Invalid data");
    }

    private boolean verifyPurchasedOfferData(PurchasedOfferRequest request) {
        var form = offerFormRepository.findByFormId(request.formId());

        if (request.data().size() != form.getFields().size()) {
            return false;
        }
        Pattern pattern;
        Matcher matcher;
        for (int i = 0; i < request.data().size(); i++) {
            var data = request.data().get(i);
            var formField = form.getFields().get(i);

            if (!formField.label().equals(data.fieldLabel()) || !formField.type().equals(data.fieldType())) {
                return false;
            }

            pattern = Pattern.compile(formField.javaRegex());
            matcher = pattern.matcher(data.fieldValue().toString());
            try {
                return matcher.matches();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

}