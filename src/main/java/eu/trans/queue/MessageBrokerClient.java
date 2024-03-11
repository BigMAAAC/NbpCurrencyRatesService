package eu.trans.queue;

import eu.trans.cache.CacheEvictedEvent;

public interface MessageBrokerClient {

    void onCacheEvicted(CacheEvictedEvent event);
}
