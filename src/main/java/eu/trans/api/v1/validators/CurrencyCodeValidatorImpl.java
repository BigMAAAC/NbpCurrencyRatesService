package eu.trans.api.v1.validators;

import eu.trans.api.v1.model.CurrencyRate;
import eu.trans.api.v1.model.CurrencyRateMother;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyCodeValidatorImpl implements ConstraintValidator<CurrencyCodeValidator, String> {

    private static final List<String> allowedCurrencyCodes = CurrencyRateMother
            .createCurrencyRates()
            .stream()
            .map(CurrencyRate::code)
            .map(String::toUpperCase)
            .toList();

    @Override
    public void initialize(final CurrencyCodeValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        return allowedCurrencyCodes.contains(s.toUpperCase());
    }
}
