package com.maghrebia.offer.mapper;

import com.maghrebia.offer.dto.OfferFormRequest;
import com.maghrebia.offer.dto.helpers.FormFieldDto;
import com.maghrebia.offer.model.records.FormField;
import com.maghrebia.offer.model.OfferForm;
import org.springframework.stereotype.Component;


@Component
public class OfferFormMapper {

    public static OfferForm toEntity(OfferFormRequest request) {

        var fields = request.fields()
                .stream().map(OfferFormMapper::toFormField)
                .toList();

        return OfferForm.builder()
                .fields(fields)
                .build();
    }

    private static FormField toFormField(FormFieldDto request) {
        return FormField.builder()
                .label(request.label())
                .type(request.type())
                .order(request.order())
                .placeholder(request.placeholder())
                .regex(request.regex())
                .rangeEnd(request.rangeEnd())
                .rangeStart(request.rangeStart())
                .regexErrorMessage(request.regexErrorMessage())
                .required(request.required())
                .selectOptions(request.selectOptions())
                .build();
    }
}
