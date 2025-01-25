package com.moquawel.marketplace.utils;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ApiResponse(
        String message,
        HttpStatus status
) {
}
