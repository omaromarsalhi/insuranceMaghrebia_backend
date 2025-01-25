package com.moquawel.tenderInvitation.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "prodInfo", url = "${search-service.prod-info.url}")
public interface ProdInfo {

    @GetMapping("/{epBidMasterId}")
    ResponseEntity<?> getProdInfoClient(@PathVariable("epBidMasterId") String epBidMasterId);

}