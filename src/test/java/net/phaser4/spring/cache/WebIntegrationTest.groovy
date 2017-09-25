package net.phaser4.spring.cache

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ContextConfiguration(classes = [TestConfiguration])
@WebMvcTest
class WebIntegrationTest extends Specification {

    @Autowired
    private MockMvc mockMvc;

    def "should cache the value returned via REST"() {
        when:
        def result1 = performTestCall()
        def result2 = performTestCall()

        then:
        result1 == [1, 1]
        result2 == [2, 2]
    }

    def performTestCall() {
        def result = mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andReturn()
        new JsonSlurper().parseText(result.getResponse().contentAsString)
    }

    @Service
    static class TestService {
        int value = 0

        @Cacheable("testCache")
        public int getValue() { ++value }
    }

    @RestController
    static class TestController {
        @Autowired
        TestService testService

        @GetMapping("/test")
        public List<Integer> test() { (1..2).collect { testService.getValue() } }
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableCaching
    @ComponentScan("net.phaser4.spring.cache")
    static class TestConfiguration {}
}
