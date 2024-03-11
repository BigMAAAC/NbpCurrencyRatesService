package eu.trans.service.strategy;

import java.io.IOException;

public interface CurrencyRateStrategy<T, R> {
    R execute(T param) throws IOException;
}
