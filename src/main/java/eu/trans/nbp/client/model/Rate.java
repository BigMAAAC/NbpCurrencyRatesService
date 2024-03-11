package eu.trans.nbp.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Rate(
        @JsonProperty("currency")
        String currency,
        @JsonProperty("effectiveDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate effectiveDate,
        @JsonProperty("code")
        String code,
        @JsonProperty("mid")
        double mid,
        @JsonProperty("bid")
        double bid,
        @JsonProperty("ask")
        double ask
) {
}
