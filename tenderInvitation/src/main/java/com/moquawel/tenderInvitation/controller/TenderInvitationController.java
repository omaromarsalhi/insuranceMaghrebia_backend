package com.moquawel.tenderInvitation.controller;


import com.moquawel.tenderInvitation.request.FilterRequest;
import com.moquawel.tenderInvitation.response.PayloadResponse;
import com.moquawel.tenderInvitation.service.TenderInvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenderInvitations")
@RequiredArgsConstructor
public class TenderInvitationController {

    private final TenderInvitationService tenderInvitationService;


    @GetMapping("/getOffers")
    public ResponseEntity<PayloadResponse> getOffers(@RequestBody @Valid FilterRequest request ){
        return ResponseEntity.ok(tenderInvitationService.getFilteredOffers(request));
    }

    @GetMapping("/getAoAndGeneralInfo/{epBidMasterId}")
    public ResponseEntity<Object> getAoAndGeneralInfo(@PathVariable String epBidMasterId){
        return ResponseEntity.ok(tenderInvitationService.getAoAndGeneralInfo(epBidMasterId));
    }

    @GetMapping("/getLotAndArticleInfo/{bidNo}")
    public ResponseEntity<Object> getLotAndArticleInfo(@PathVariable String bidNo){
        return ResponseEntity.ok(tenderInvitationService.getLotAndArticleInfo(bidNo));
    }

    @GetMapping("/getProdInfo/{bidNo}")
    public ResponseEntity<Object> getProdInfo(@PathVariable String bidNo){
        return ResponseEntity.ok(tenderInvitationService.getProdInfo(bidNo));
    }

    @GetMapping("/getAgrementInfo/{bidNo}")
    public ResponseEntity<Object> getAgrementInfo(@PathVariable String bidNo){
        return ResponseEntity.ok(tenderInvitationService.getAgrementInfo(bidNo));
    }

    @GetMapping("/getTermsOfReferenceInfo/{bidNo}")
    public ResponseEntity<Object> getTermsOfReferenceInfo(@PathVariable String bidNo){
        return ResponseEntity.ok(tenderInvitationService.getTermsOfReferenceInfo(bidNo));
    }



}
