package com.example.demo.config;

import com.example.demo.service.rateLimiter.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    static final private int TOO_MANY_REQUESTS = 429;
    @Autowired
    private RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = request.getRemoteAddr(); // Use userId, token, or other identifier if needed

        if (!rateLimiterService.isAllowed(key)) {
            response.setStatus(RateLimitInterceptor.TOO_MANY_REQUESTS);
            response.getWriter().write("Rate limit exceeded. Please try again later.");
            return false;
        }

        return true;
    }
}
