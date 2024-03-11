package eu.trans.api.v1.indicator;


import eu.trans.cache.CacheEvictedEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Component
public class MockHealthIndicator implements CustomHealthIndicator {

    private static final String STATUS_INFO = "Every call to the '/refresh-cache/{currencyCode}' endpoint changes the status from up to down and vice versa.";

    private boolean brokerConnection = true;
    private AtomicInteger emittedEventsCount = new AtomicInteger(0);


    @Override
    public Health health() {

        Health.Builder builder = new Health.Builder();

        if (brokerConnection) {
            builder.up();
        } else {
            builder.down();
        }

        builder.withDetail("brokerConnection", brokerConnection ? "up" : "down");
        builder.withDetail("emittedEvents", emittedEventsCount.get());
        builder.withDetail("statusInfo", STATUS_INFO);

        return builder.build();
    }

    @EventListener
    public void onCacheEvicted(final CacheEvictedEvent event) {
        this.brokerConnection = !this.brokerConnection;
        log.info("Cache evicted event received: {}", event);
        emittedEventsCount.incrementAndGet();
    }
}
