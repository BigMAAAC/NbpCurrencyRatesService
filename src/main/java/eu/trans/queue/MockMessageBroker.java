package eu.trans.queue;

import eu.trans.cache.CacheEvictedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
@RequiredArgsConstructor
public class MockMessageBroker implements MessageBrokerClient {

    private final ScheduledExecutorService scheduler;

    @EventListener
    public void onCacheEvicted(final CacheEvictedEvent event) {
        final String message = "Cache '" + event.getKey() + "' evicted for key '" + event.getValue() + "'";
        send(message);
    }

    private void send(final String message) {
        scheduler.schedule(() -> log.info("Event broadcast on the queue: {}", message), 1, TimeUnit.SECONDS);
    }
}
