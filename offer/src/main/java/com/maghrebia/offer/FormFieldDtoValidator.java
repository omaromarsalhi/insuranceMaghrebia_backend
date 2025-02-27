package com.maghrebia.offer;

import com.maghrebia.offer.dto.helpers.FormFieldDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

public class FormFieldDtoValidator implements ConstraintValidator<FormFieldDtoValid, FormFieldDto> {

    @Override
    public boolean isValid(FormFieldDto formField, ConstraintValidatorContext context) {
        boolean isValid = true;
        String type = formField.type();

        // Validate selectOptions for select, checkbox, radio
        if (type != null && (type.equals("select") || type.equals("checkbox"))) {
            List<String> options = formField.selectOptions();
            String placeholder = formField.placeholder();


            if (placeholder == null || placeholder.isBlank()) {
                context.buildConstraintViolationWithTemplate("Placeholder is required")
                        .addPropertyNode("placeholder")
                        .addConstraintViolation();
                isValid = false;
            }
            if (options == null || options.isEmpty()) {
                context.buildConstraintViolationWithTemplate("At least one option is required for select, checkbox, or radio fields")
                        .addPropertyNode("selectOptions")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        // Validate regex and regexErrorMessage for text and email
        if (type != null && (type.equals("text") || type.equals("email"))) {
            String regex = formField.regex();
            String regexError = formField.regexErrorMessage();
            String placeholder = formField.placeholder();


            if (placeholder == null || placeholder.isBlank()) {
                context.buildConstraintViolationWithTemplate("Placeholder is required")
                        .addPropertyNode("placeholder")
                        .addConstraintViolation();
                isValid = false;
            }

            // Check regex presence
            if (regex == null || regex.isBlank()) {
                context.buildConstraintViolationWithTemplate("Regex is required for text or email fields")
                        .addPropertyNode("regex")
                        .addConstraintViolation();
                isValid = false;
            } else {
                // Validate regex pattern
                if (!isValidRegexPattern(regex)) {
                    context.buildConstraintViolationWithTemplate("Invalid TypeScript regex format. Example: /pattern/modifiers")
                            .addPropertyNode("regex")
                            .addConstraintViolation();
                    isValid = false;
                }
            }

            // Check regex error message presence
            if (regexError == null || regexError.isBlank()) {
                context.buildConstraintViolationWithTemplate("Regex error message is required for text or email fields")
                        .addPropertyNode("regexErrorMessage")
                        .addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean isValidRegexPattern(String regex) {
        return Pattern.matches("^/(.+)/[gimsuy]*$", regex);
    }
}
