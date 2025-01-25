package com.moquawel.marketplace.mkservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record Field(

        @NotBlank(message = "Field name cannot be blank")
        @Size(min = 2, max = 50, message = "Field name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Field type cannot be blank")
        @Pattern(regexp = "string|list<string>|list<integer>|integer|checkbox|float", message = "Wrong Field Type")
        String type,

        @Size(min = 1, message = "Field values must contain at least one value")
        List<@NotBlank(message = "Field value cannot be blank") String> values
) {}