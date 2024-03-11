package eu.trans.api.v1.indicator;


import com.rabbitmq.client.ShutdownNotifier;
import eu.trans.cache.CacheEvictedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


@Log4j2
@ConditionalOnProperty(name = "RabbitMQHealthIndicator.active", havingValue = "true")
@Component
@RequiredArgsConstructor
public class RabbitMQHealthIndicator implements CustomHealthIndicator {

    private final RabbitTemplate rabbitTemplate;
    private AtomicInteger emittedEventsCount = new AtomicInteger(0);

    @Override
    public Health health() {

        Health.Builder builder = new Health.Builder();

        try {
            rabbitTemplate.execute(ShutdownNotifier::isOpen);
            builder.up().withDetail("brokerConnection", "UP");
        } catch (Exception e) {
            builder.down(e).withDetail("brokerConnection", "DOWN");
        }

        builder.withDetail("emittedEvents", emittedEventsCount.get());
        return builder.build();
    }

    @EventListener
    public void onCacheEvicted(final CacheEvictedEvent event) {
        log.info("Cache evicted event received: {}", event);
        emittedEventsCount.incrementAndGet();
    }
}
