package com.moquawel.tenderInvitation.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "aoAndGeneralInfoClient", url = "${search-service.AO-and-general-info.url}")
public interface AoAndGeneralInfoClient {
    @GetMapping("/{epBidMasterId}")
    ResponseEntity<?> getAoAndGeneralInfoClient(@PathVariable("epBidMasterId") String epBidMasterId);
}