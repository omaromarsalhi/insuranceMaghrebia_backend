package com.moquawel.tenderInvitation.request;


import java.time.LocalDateTime;
import jakarta.validation.constraints.*;


public record FilterRequest(

        @PastOrPresent(message = "Public date must be in the past or present")
        LocalDateTime publicDt,


        @Future(message = "BD Receive End date must be in the future")
        LocalDateTime bdRecvEndDt,

        @NotEmpty(message = "Category must not be empty")
        @NotNull(message = "Category must not be null")
        @Size(min = 3, max = 50, message = "Category must be between 3 and 50 characters")
        @Pattern(regexp = "^[A-Za-z]+$", message = "Category must only contain alphabetic characters")
        String category
) {
}

