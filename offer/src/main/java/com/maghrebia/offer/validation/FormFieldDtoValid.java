package com.maghrebia.offer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FormFieldDtoValidator.class)
public @interface FormFieldDtoValid {
    String message() default "Invalid form field data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
