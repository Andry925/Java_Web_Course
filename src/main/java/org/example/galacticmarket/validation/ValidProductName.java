package org.example.galacticmarket.validation;



import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidProductNameImpl.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductName {
    String message() default "Product name must start with one of the prefixes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] prefixes() default {"galaxy"};
}