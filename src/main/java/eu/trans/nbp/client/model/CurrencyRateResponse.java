package eu.trans.nbp.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyRateResponse(
        @JsonProperty("table")
        String table,
        @JsonProperty("no")
        String no,
        @JsonProperty("effectiveDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate effectiveDate,
        @JsonProperty("currency")
        String currency,
        @JsonProperty("code")
        String code,
        @JsonProperty("rates")
        List<Rate> rates
) {
}
