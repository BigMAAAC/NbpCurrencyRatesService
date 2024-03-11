package eu.trans.nbp.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trans.api.v1.exceptions.CurrencyRateRetrievalException;
import eu.trans.nbp.client.model.CurrencyRateResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NbpClient {

    public static final String A_NBP_TABLE = "A";
    public static final String C_NBP_TABLE = "C";

    @Value("${npb.api.url}")
    private String nbpApiUrl;
    private String popularExchangeRatesUrl;
    private String currentRatesUrl;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void initUrls() {
        this.popularExchangeRatesUrl = nbpApiUrl + "exchangerates/tables/" + A_NBP_TABLE;
        this.currentRatesUrl = nbpApiUrl + "exchangerates/rates/";
    }

    public List<CurrencyRateResponse> getPopularExchangeRatesByDate(final LocalDate currencyDate) throws CurrencyRateRetrievalException {

        return call(popularExchangeRatesUrl + "/" + currencyDate, new TypeReference<>() {
        });
    }

    public CurrencyRateResponse getExchangeCurrencyRatesByCode(final String currencyCode, final LocalDate date, final String nbpApiTableType) throws CurrencyRateRetrievalException {
        final String dateFrom = date.minusDays(5).toString();
        return call(currentRatesUrl + nbpApiTableType + "/" + currencyCode + "/" + dateFrom + "/" + date.toString(), new TypeReference<>() {
        });
    }

    private <T> T call(final String url, final TypeReference<T> typeReference) throws CurrencyRateRetrievalException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CurrencyRateRetrievalException("Unexpected code " + response, null);
            }
            return objectMapper.readValue(response.body().string(), typeReference);
        } catch (IOException e) {
            throw new CurrencyRateRetrievalException("Failed to call URL: " + url, e);
        }
    }
}
