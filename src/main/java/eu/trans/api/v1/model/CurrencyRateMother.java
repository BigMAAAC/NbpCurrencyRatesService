package eu.trans.api.v1.model;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class CurrencyRateMother {

    public static List<CurrencyRate> createCurrencyRates() {
        return List.of(
                CurrencyRate.builder()
                        .currency("dolar amerykański")
                        .code("USD")
                        .mid(3.7893)
                        .bid(3.7293)
                        .ask(3.8493)
                        .date(LocalDate.now())
                        .build(),
                CurrencyRate.builder()
                        .currency("dolar amerykański")
                        .code("USD")
                        .mid(3.7893)
                        .bid(3.7293)
                        .ask(3.8493)
                        .date(LocalDate.now().minusDays(1))
                        .build(),
                CurrencyRate.builder()
                        .currency("dolar australijski")
                        .code("AUD")
                        .mid(2.8963)
                        .bid(2.8363)
                        .ask(2.9563)
                        .date(LocalDate.now())
                        .build(),
                CurrencyRate.builder()
                        .currency("dolar australijski")
                        .code("AUD")
                        .mid(2.8963)
                        .bid(2.8363)
                        .ask(2.9563)
                        .date(LocalDate.now().minusDays(1))
                        .build(),
                CurrencyRate.builder()
                        .currency("dolar kanadyjski")
                        .code("CAD")
                        .mid(2.9783)
                        .bid(2.9183)
                        .ask(3.0383)
                        .date(LocalDate.now())
                        .build(),
                CurrencyRate.builder()
                        .currency("euro")
                        .code("EUR")
                        .mid(4.5563)
                        .bid(4.4963)
                        .ask(4.6163)
                        .date(LocalDate.now())
                        .build(),
                CurrencyRate.builder()
                        .currency("forint (Węgry)")
                        .code("HUF")
                        .mid(0.0133)
                        .bid(0.0073)
                        .ask(0.0193)
                        .date(LocalDate.now())
                        .build(),
                CurrencyRate.builder()
                        .currency("frank szwajcarski")
                        .code("CHF")
                        .mid(4.1563)
                        .bid(4.0963)
                        .ask(4.2163)
                        .date(LocalDate.now())
                        .build(),
                CurrencyRate.builder()
                        .currency("funt szterling")
                        .code("GBP")
                        .mid(5.1563)
                        .bid(5.0963)
                        .ask(5.2163)
                        .date(LocalDate.now())
                        .build(),
                CurrencyRate.builder()
                        .currency("jen (Japonia)")
                        .code("JPY")
                        .mid(0.0363)
                        .bid(0.0263)
                        .ask(0.0463)
                        .date(LocalDate.now())
                        .build());
    }
}
