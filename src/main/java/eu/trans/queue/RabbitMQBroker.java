package eu.trans.queue;

import eu.trans.cache.CacheEvictedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@ConditionalOnProperty(name = "RabbitMQBroker.active", havingValue = "true")
@Component
@RequiredArgsConstructor
public class RabbitMQBroker implements MessageBrokerClient {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingkey;

    @EventListener
    public void onCacheEvicted(final CacheEvictedEvent event) {
        final String message = "Cache '" + event.getKey() + "' evicted for key '" + event.getValue() + "'";
        send(message);
    }

    private void send(final String currencyCode) {
        final String message = "Cache for currency " + currencyCode + " has been refreshed";
        rabbitTemplate.convertAndSend(exchange, routingkey, message);
        log.info("Event broadcast on the queue: {}", message);
    }
}
