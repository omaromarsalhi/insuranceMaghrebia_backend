package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.dto.PurchasedOfferRequest;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.mapper.PurchasedOfferMapper;
import com.maghrebia.offer.model.records.FilteredCategory;
import com.maghrebia.offer.repository.OfferFormRepository;
import com.maghrebia.offer.repository.OfferRepository;
import com.maghrebia.offer.repository.PurchasedOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


@AllArgsConstructor
@Service
public class PurchasedOfferService {

    private final PurchasedOfferRepository purchasedOfferRepository;
    private final OfferFormRepository offerFormRepository;


    public String create(PurchasedOfferRequest request) {
//        var testResult=verifyPurchasedOfferData(request);
//        if(testResult){
//            System.out.println("done");
        purchasedOfferRepository.save(PurchasedOfferMapper.toEntity(request));
//            return "success";
//        }
//        System.out.println("failed");
        return "error";
    }

    private boolean verifyPurchasedOfferData(PurchasedOfferRequest request) {
        var form = offerFormRepository.findByFormId(request.formId());
        System.out.println(form);

//        if (request.data().size() != form.getFields().size()) {
//            return false;
//        }

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

        for (int i = 0; i < request.data().size(); i++) {
            var data = request.data().get(i);
            var formField = form.getFields().get(i);

            if (!formField.label().equals(data.fieldLabel()) || !formField.type().equals(data.fieldType())) {
                System.out.println("1");
                System.out.println(data.fieldValue());
                System.out.println(data.fieldType());
                System.out.println(formField.label());
                return false;
            }

            String regex = formField.regex().replace("'", "\\'");
            String fieldValue = data.fieldValue().toString().replace("'", "\\'");
            String script = "new RegExp('" + regex + "').test('" + fieldValue + "');";

            try {
                boolean result = (boolean) engine.eval(script);
                if (!result) {
                    System.out.println("2");
                    System.out.println(regex);
                    System.out.println(fieldValue);
                    System.out.println(data.fieldType());
                    return false;
                }
            } catch (Exception e) {
                System.out.println("3");
                System.out.println(regex);
                System.out.println(fieldValue);
                System.out.println(data.fieldType());
                return false;
            }
        }
        return true;
    }

}