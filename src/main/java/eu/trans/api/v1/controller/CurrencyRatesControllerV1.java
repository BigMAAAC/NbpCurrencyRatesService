package eu.trans.api.v1.controller;

import eu.trans.api.v1.ControllerV1;
import eu.trans.api.v1.model.CurrencyRate;
import eu.trans.api.v1.validators.CurrencyCodeValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;


public interface CurrencyRatesControllerV1 extends ControllerV1 {

    ResponseEntity<List<CurrencyRate>> getCurrencyRates(LocalDate date);

    ResponseEntity<CurrencyRate> getCurrencyRatesByCurrencyCode(@Valid @CurrencyCodeValidator(message = "No valid currency code") String id);
}
