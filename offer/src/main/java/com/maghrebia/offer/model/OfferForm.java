package com.maghrebia.offer.model;


import com.maghrebia.offer.model.records.FormField;
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
@Document(collection = "offer_form")
public class OfferForm {

    @Id
    private String formId;

    private List<FormField> fields;

}
