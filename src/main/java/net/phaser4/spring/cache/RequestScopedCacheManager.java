package net.phaser4.spring.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestScopedCacheManager implements CacheManager {

    private static final ThreadLocal<Map<String, Cache>> threadLocalCache = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public Cache getCache(String cacheName) {
        return threadLocalCache.get().computeIfAbsent(cacheName, name -> createCache(cacheName));
    }

    public Collection<String> getCacheNames() {
        return threadLocalCache.get().keySet();
    }

    public void reset() {
        threadLocalCache.get().clear();
    }

    private Cache createCache(String cacheName) {
        return new ConcurrentMapCache(cacheName);
    }
}
