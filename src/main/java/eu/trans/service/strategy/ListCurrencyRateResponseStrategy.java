package eu.trans.service.strategy;

import eu.trans.api.v1.exceptions.CurrencyRateRetrievalException;
import eu.trans.api.v1.model.CurrencyRate;
import eu.trans.nbp.client.NbpClient;
import eu.trans.nbp.client.model.CurrencyRateResponse;
import eu.trans.nbp.client.model.Rate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class ListCurrencyRateResponseStrategy implements CurrencyRateStrategy<LocalDate, List<CurrencyRate>> {

    private final NbpClient nbpClient;

    @Override
    public List<CurrencyRate> execute(final LocalDate currencyDate) {
        log.info("Getting popular currency rates by date: {}", currencyDate);

        return nbpClient.getPopularExchangeRatesByDate(currencyDate)
                .stream()
                .flatMap(response -> response.rates().stream())
                .map(rate -> mapToCurrencyRate(rate, currencyDate))
                .toList();
    }

    private CurrencyRate mapToCurrencyRate(final Rate rate, final LocalDate currencyDate) {
        final CurrencyRate.CurrencyRateBuilder builder = CurrencyRate.builder()
                .currency(rate.currency())
                .code(rate.code())
                .mid(rate.mid())
                .date(currencyDate);

        try {
            final CurrencyRateResponse currencyRate = nbpClient.getExchangeCurrencyRatesByCode(rate.code(), currencyDate, NbpClient.C_NBP_TABLE);
            currencyRate.rates().stream()
                    .filter(r -> r.effectiveDate().equals(currencyDate))
                    .findFirst()
                    .ifPresent(r -> {
                        builder.bid(r.bid());
                        builder.ask(r.ask());
                    });

            return builder
                    .build();

        } catch (CurrencyRateRetrievalException e) {
            log.warn("Error while getting currency rate by code: ", e);
            return builder
                    .build();
        }
    }
}
