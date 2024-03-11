package eu.trans.api.v1.exceptions;

public class CurrencyRateRetrievalException extends RuntimeException {

    public CurrencyRateRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}