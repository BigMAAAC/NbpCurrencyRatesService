package eu.trans.api.v1.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CurrencyCodeValidatorImpl.class)
@Documented
public @interface CurrencyCodeValidator {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
