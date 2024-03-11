package eu.trans.service;

import eu.trans.api.v1.model.CurrencyRate;
import eu.trans.cache.CacheEvictedEvent;
import eu.trans.nbp.client.NbpClient;
import eu.trans.persistance.InMemoryCurrencyRateRepository;
import eu.trans.service.strategy.CurrencyRateResponseStrategy;
import eu.trans.service.strategy.ListCurrencyRateResponseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CurrencyRatesService {

    private final CacheManager cacheManager;
    private final ApplicationEventPublisher eventPublisher;
    private final ListCurrencyRateResponseStrategy listCurrencyRateResponseStrategy;
    private final CurrencyRateResponseStrategy currencyRateResponseStrategy;
    private final InMemoryCurrencyRateRepository inMemoryCurrencyRateRepository;

    public CurrencyRatesService(final NbpClient nbpClient,
                                final CacheManager cacheManager,
                                final ApplicationEventPublisher eventPublisher,
                                final InMemoryCurrencyRateRepository inMemoryCurrencyRateRepository) {
        this.cacheManager = cacheManager;
        this.eventPublisher = eventPublisher;
        this.listCurrencyRateResponseStrategy = new ListCurrencyRateResponseStrategy(nbpClient);
        this.currencyRateResponseStrategy = new CurrencyRateResponseStrategy(nbpClient);
        this.inMemoryCurrencyRateRepository = inMemoryCurrencyRateRepository;
    }

    @Cacheable(value = "currencyRates", key = "'date-' + #date.toString()", unless = "#result == null or #result.isEmpty()")
    public List<CurrencyRate> getCurrencyRatesByDate(final LocalDate date) {
        log.info("Getting currency rates by date: {}", date);
        final List<CurrencyRate> currencyRates = listCurrencyRateResponseStrategy.execute(date);
        persistData(currencyRates);
        return currencyRates;
    }

    @Cacheable(value = "currencyRates", key = "'code-' + #currencyCode + '-date-' + #date.toString()", unless = "#result == null")
    public CurrencyRate getCurrencyRatesByCurrencyCode(final String currencyCode, final LocalDate date) {
        log.info("Getting currency rates by currency code: {} and date: {}", currencyCode, date);
        final CurrencyRate currencyRate = currencyRateResponseStrategy.execute(new CurrencyRateResponseStrategy.CurrencyRateRequest(currencyCode, date));
        persistData(Collections.singletonList(currencyRate));
        return currencyRate;
    }

    public void evictAllCaches(final String currencyCode) {
        log.info("Evicting all caches for currency code: {}", currencyCode);
        LocalDate today = LocalDate.now();
        String key = "code-" + currencyCode + "-date-" + today.toString();
        cacheManager.getCache("currencyRates").evictIfPresent(key);
        eventPublisher.publishEvent(new CacheEvictedEvent(this, "currencyCode", currencyCode));
        final CurrencyRate currencyRate = currencyRateResponseStrategy.execute(new CurrencyRateResponseStrategy.CurrencyRateRequest(currencyCode, today));
        persistData(Collections.singletonList(currencyRate));
    }

    private void persistData(final List<CurrencyRate> currencyRates) {
        currencyRates
                .forEach(inMemoryCurrencyRateRepository::save);
    }
}
