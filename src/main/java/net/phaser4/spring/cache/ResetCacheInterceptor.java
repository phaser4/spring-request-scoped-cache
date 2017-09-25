package net.phaser4.spring.cache;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResetCacheInterceptor extends HandlerInterceptorAdapter {

    private final RequestScopedCacheManager cacheManager;

    public ResetCacheInterceptor(RequestScopedCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        cacheManager.reset();
    }
}
