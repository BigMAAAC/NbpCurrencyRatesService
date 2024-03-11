package eu.trans.api.v1.model;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CurrencyRate(
        String currency,
        String code,
        double mid,
        double bid,
        double ask,
        LocalDate date
) {
}

