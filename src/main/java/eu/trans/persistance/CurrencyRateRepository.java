package eu.trans.persistance;

import eu.trans.api.v1.model.CurrencyRate;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRateRepository {

    void save(CurrencyRate currencyRate);

    List<CurrencyRate> findAll();

    List<CurrencyRate> findByDate(LocalDate date);

    CurrencyRate findByCurrencyCodeAndDate(String currencyCode, LocalDate date);

}
