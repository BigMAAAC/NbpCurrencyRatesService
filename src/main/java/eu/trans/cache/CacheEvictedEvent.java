package eu.trans.cache;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CacheEvictedEvent extends ApplicationEvent {
    private final String key;
    private final String value;

    public CacheEvictedEvent(Object source, String key, String value) {
        super(source);
        this.key = key;
        this.value = value;
    }
}
