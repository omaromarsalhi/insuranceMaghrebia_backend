package com.maghrebia.signatureprocessing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignatureRequest {

    String base64_data;

    String fullName;

    String cin;
}
