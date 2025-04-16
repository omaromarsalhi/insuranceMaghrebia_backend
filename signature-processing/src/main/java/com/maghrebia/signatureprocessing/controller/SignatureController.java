package com.maghrebia.signatureprocessing.controller;

import com.maghrebia.signatureprocessing.entity.SignatureRequest;
import com.maghrebia.signatureprocessing.service.SignatureProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/signature")
@RequiredArgsConstructor
public class SignatureController {

    final private SignatureProcessingService signatureService;

    @PostMapping()
    public ResponseEntity<String> verifySignature(@RequestBody SignatureRequest request) {
        return signatureService.verifyService(request);
    }


}
