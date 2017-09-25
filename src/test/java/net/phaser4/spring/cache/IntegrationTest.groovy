package net.phaser4.spring.cache

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [TestConfiguration])
class IntegrationTest extends Specification {

    @Autowired
    TestService testService

    @Autowired
    RequestScopedCacheManager cacheManager

    def "should cache the returned value"() {
        when:
        def value1 = testService.getValue()
        def value2 = testService.getValue()

        then:
        value1 == value2
        cacheManager.cacheNames.contains("testCache")
    }

    static class TestService {
        int value = 0

        @Cacheable("testCache")
        public int getValue() { ++value }
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableCaching
    static class TestConfiguration {
        @Bean
        public TestService testService() { new TestService() }
    }
}
