package org.example.galacticmarket.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ProductNameValidatorImpl implements ConstraintValidator<ProductNameValidator, String> {

    private String[] prefixes;

    @Override
    public void initialize(ProductNameValidator constraintAnnotation) {
        this.prefixes = constraintAnnotation.prefixes();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Arrays.stream(prefixes)
                .anyMatch(prefix -> value.toLowerCase().startsWith(prefix.toLowerCase()));
    }
}