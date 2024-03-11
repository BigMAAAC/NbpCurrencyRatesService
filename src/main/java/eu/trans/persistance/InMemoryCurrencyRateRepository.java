package eu.trans.persistance;

import eu.trans.api.v1.model.CurrencyRate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryCurrencyRateRepository implements CurrencyRateRepository {

    private final List<CurrencyRate> currencyRates = new ArrayList<>();

    @Override
    public void save(CurrencyRate currencyRate) {
        currencyRates.add(currencyRate);
    }

    @Override
    public List<CurrencyRate> findAll() {
        return new ArrayList<>(currencyRates);
    }

    @Override
    public List<CurrencyRate> findByDate(LocalDate date) {
        return currencyRates.stream()
                .filter(currencyRate -> currencyRate.date().equals(date))
                .toList();
    }

    @Override
    public CurrencyRate findByCurrencyCodeAndDate(String currencyCode, LocalDate date) {
        return currencyRates.stream()
                .filter(currencyRate -> currencyRate.code().equals(currencyCode) && currencyRate.date().equals(date))
                .findFirst()
                .orElse(null);
    }
}
