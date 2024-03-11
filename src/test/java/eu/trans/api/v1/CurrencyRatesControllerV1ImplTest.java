package eu.trans.api.v1;

import eu.trans.api.v1.controller.CurrencyRatesControllerV1Impl;
import eu.trans.api.v1.model.CurrencyRate;
import eu.trans.service.CurrencyRatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CurrencyRatesControllerV1ImplTest {

    @Mock
    private CurrencyRatesService currencyRatesService;

    @InjectMocks
    private CurrencyRatesControllerV1Impl currencyRatesControllerV1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrencyRatesByCurrencyCodeReturnsRates() throws IOException {
        String currencyCode = "USD";
        CurrencyRate expectedRates = CurrencyRate.builder()
                .currency("dolar amerykański")
                .code("USD")
                .mid(3.7893)
                .bid(3.7293)
                .ask(3.8493)
                .date(LocalDate.now())
                .build();
        when(currencyRatesService.getCurrencyRatesByCurrencyCode(currencyCode, LocalDate.now())).thenReturn(expectedRates);

        ResponseEntity<CurrencyRate> response = currencyRatesControllerV1.getCurrencyRatesByCurrencyCode(currencyCode);

        assertEquals(expectedRates, response.getBody());
    }

    @Test
    void getCurrencyRatesByCurrencyCodeReturnsNotFoundForEmptyRates() throws IOException {
        String currencyCode = "USD";
        when(currencyRatesService.getCurrencyRatesByCurrencyCode(currencyCode, LocalDate.now())).thenReturn(null);

        ResponseEntity<CurrencyRate> response = currencyRatesControllerV1.getCurrencyRatesByCurrencyCode(currencyCode);

        assertEquals(404, response.getStatusCodeValue());
    }
}
