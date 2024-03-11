package eu.trans.api.v1.controller;

import eu.trans.api.v1.docs.CurrencyRatesControllerV1Doc;
import eu.trans.api.v1.docs.GetCurrencyRatesByCurrencyCodeDoc;
import eu.trans.api.v1.docs.GetCurrencyRatesDoc;
import eu.trans.api.v1.docs.RefreshCacheByCurrencyCodeDoc;
import eu.trans.api.v1.model.CurrencyRate;
import eu.trans.api.v1.validators.CurrencyCodeValidator;
import eu.trans.service.CurrencyRatesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CurrencyRatesControllerV1Doc
public class CurrencyRatesControllerV1Impl implements CurrencyRatesControllerV1 {

    private final CurrencyRatesService currencyRatesService;

    @Override
    @GetCurrencyRatesDoc
    @GetMapping("/rates")
    public ResponseEntity<List<CurrencyRate>> getCurrencyRates(
            @RequestParam(
                    required = false,
                    name = "currencyDate",
                    defaultValue = "#{T(java.time.LocalDate).now()}"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate currencyDate) {


        List<CurrencyRate> currencyRatesByDate = currencyRatesService.getCurrencyRatesByDate(currencyDate);
        if (null == currencyRatesByDate || currencyRatesByDate.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .ok(currencyRatesByDate);
    }


    @Override
    @GetCurrencyRatesByCurrencyCodeDoc
    @GetMapping("/rates/{id}")
    public ResponseEntity<CurrencyRate> getCurrencyRatesByCurrencyCode(
            @PathVariable
            @CurrencyCodeValidator(message = "No valid currency code")
            @Valid final String id) {

        final CurrencyRate currencyRatesByCurrencyCode = currencyRatesService.getCurrencyRatesByCurrencyCode(id, LocalDate.now());

        if (Objects.isNull(currencyRatesByCurrencyCode)) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .ok(currencyRatesByCurrencyCode);
    }

    @PostMapping("/refresh-cache/{currencyCode}")
    @RefreshCacheByCurrencyCodeDoc
    @CacheEvict(value = "currencyRates", key = "'code-' + #currencyCode + '-date-*'")
    public ResponseEntity<Void> refreshCacheForCurrency(
            @PathVariable
            @CurrencyCodeValidator(message = "No valid currency code")
            @Valid final String currencyCode) {
        currencyRatesService.evictAllCaches(currencyCode);
        return ResponseEntity
                .noContent()
                .build();
    }
}

