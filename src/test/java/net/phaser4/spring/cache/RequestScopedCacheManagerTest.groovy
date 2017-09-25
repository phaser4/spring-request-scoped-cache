package net.phaser4.spring.cache

import org.springframework.cache.Cache
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class RequestScopedCacheManagerTest extends Specification {

    RequestScopedCacheManager cacheManager

    def setup() {
        cacheManager = new RequestScopedCacheManager()
    }

    def cleanup() {
        cacheManager.reset()
    }

    def "should create a new cache"() {
        when:
        Cache cache = cacheManager.getCache("cache1")
        cache.put("key", "value")

        then:
        cache.get("key").get() == "value"
    }

    def "should list the cache names"() {
        given:
        cacheNames.each { cacheManager.getCache(it) }

        when:
        def result = cacheManager.getCacheNames()

        then:
        result.sort() == cacheNames.sort()

        where:
        cacheNames = ["cache333", "cache444"]
    }

    def "should not share cache between threads"() {
        given:
        cacheManager.getCache(cacheName).put(key, "A")

        when:
        def future = CompletableFuture.supplyAsync({ ->
            cacheManager.getCache(cacheName).put(key, "B")
            cacheManager.getCache(cacheName).get(key) })

        def valueFromThisThread = cacheManager.getCache(cacheName).get(key)
        def valueFromAnotherThread = future.get()

        then:
        valueFromThisThread.get() == "A"
        valueFromAnotherThread.get() == "B"

        where:
        key = "key"
        cacheName = "cache"
    }
}
