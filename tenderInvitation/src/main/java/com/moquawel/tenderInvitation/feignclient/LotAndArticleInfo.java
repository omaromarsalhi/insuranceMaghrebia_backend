package com.moquawel.tenderInvitation.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "lotAndArticleInfo", url = "${search-service.lot-and-article-info.url}")
public interface LotAndArticleInfo {
    @GetMapping
    ResponseEntity<?> getLotAndArticleInfoClient(@RequestParam("bidNo") String bidNo);

}