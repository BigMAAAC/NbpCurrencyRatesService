package eu.trans.service.strategy;

import eu.trans.api.v1.exceptions.CurrencyRateRetrievalException;
import eu.trans.api.v1.model.CurrencyRate;
import eu.trans.nbp.client.NbpClient;
import eu.trans.nbp.client.model.CurrencyRateResponse;
import eu.trans.nbp.client.model.Rate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;

@Log4j2
@RequiredArgsConstructor
public class CurrencyRateResponseStrategy implements CurrencyRateStrategy<CurrencyRateResponseStrategy.CurrencyRateRequest, CurrencyRate> {

    private final NbpClient nbpClient;

    @Override
    public CurrencyRate execute(final CurrencyRateRequest currencyRateRequest) {
        log.info("Getting currency rate by code: {}", currencyRateRequest);
        final CurrencyRateResponse currencyMidRate = fetchCurrencyRateResponse(currencyRateRequest, NbpClient.A_NBP_TABLE, true);
        final CurrencyRateResponse currencyBidAskRate = fetchCurrencyRateResponse(currencyRateRequest, NbpClient.C_NBP_TABLE, false);
        return buildCurrencyRate(currencyMidRate, currencyBidAskRate);
    }

    private CurrencyRateResponse fetchCurrencyRateResponse(final CurrencyRateRequest currencyRateRequest, final String tableType, final boolean throwErrorOnFailure) {
        final String currencyCode = currencyRateRequest.currencyCode();
        final LocalDate date = currencyRateRequest.date();
        try {
            return nbpClient.getExchangeCurrencyRatesByCode(currencyCode, date, tableType);
        } catch (CurrencyRateRetrievalException e) {
            if (throwErrorOnFailure) {
                log.warn("Error while getting bid/ask rate by code: {}", currencyRateRequest, e);
                throw e;
            } else {
                log.warn("Error while getting bid/ask rate by code: {} end skipped", currencyRateRequest, e);
                return null;
            }
        }
    }

    private static CurrencyRate buildCurrencyRate(final CurrencyRateResponse midRateResponse, final CurrencyRateResponse bidAskRateResponse) {
        final Rate lastRate = getLastRate(midRateResponse);
        final CurrencyRate.CurrencyRateBuilder builder = CurrencyRate.builder()
                .currency(midRateResponse.currency())
                .code(midRateResponse.code())
                .mid(lastRate != null ? lastRate.mid() : 0)
                .date(lastRate != null ? lastRate.effectiveDate() : null);

        if (bidAskRateResponse != null) {
            bidAskRateResponse.rates().stream()
                    .filter(rate -> rate.effectiveDate().equals(lastRate.effectiveDate()))
                    .findFirst()
                    .ifPresent(rate -> {
                        builder.bid(rate.bid());
                        builder.ask(rate.ask());
                    });
        }
        return builder.build();
    }

    private static Rate getLastRate(final CurrencyRateResponse midRateResponse) {
        return midRateResponse.rates().stream()
                .reduce((a1, a2) -> a2)
                .orElse(null);
    }

    public record CurrencyRateRequest(
            String currencyCode,
            LocalDate date) {
    }
}

