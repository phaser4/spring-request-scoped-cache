package net.phaser4.spring.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class RequestScopedCacheConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ResetCacheInterceptor resetCacheInterceptor;

    @Bean
    public RequestScopedCacheManager requestScopedCacheManager() {
        return new RequestScopedCacheManager();
    }

    @Bean
    public ResetCacheInterceptor resetCacheInterceptor(RequestScopedCacheManager cacheManager) {
        return new ResetCacheInterceptor(cacheManager);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(resetCacheInterceptor);
    }
}