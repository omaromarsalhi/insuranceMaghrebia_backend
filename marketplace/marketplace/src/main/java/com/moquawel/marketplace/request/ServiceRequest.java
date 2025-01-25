package com.moquawel.marketplace.request;

import com.moquawel.marketplace.mkservice.Field;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ServiceRequest(

        @Size(max = 36, message = "Service ID should not exceed 36 characters")
        String serviceId,

        @NotBlank(message = "Service Name cannot be blank")
        @Size(min = 3, max = 100, message = "Service Name must be between 3 and 100 characters")
        String serviceName,

        @NotNull(message = "Fields cannot be null")
        @Size(min = 1, message = "There must be at least one field")
        @Valid
        List<@Valid Field> fields,

        @NotBlank(message = "Category ID cannot be blank")
        @NotNull(message = "Category ID cannot be null")
        @Size(max = 36, message = "Category ID should not exceed 36 characters")
        String categoryId

) {
}


