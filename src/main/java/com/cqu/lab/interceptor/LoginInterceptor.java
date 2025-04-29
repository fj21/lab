package com.cqu.lab.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cqu.lab.constant.Constants;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * 登录拦截器
 */
@Slf4j
@Order(0)
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // Log the request details for debugging
        log.info("LoginInterceptor: Processing request URI: {}", requestURI);
        log.info("LoginInterceptor: Request method: {}", request.getMethod());
        log.info("LoginInterceptor: Token header: {}", request.getHeader("token"));

        // Skip authentication for login and register endpoints
        if (requestURI.contains("login") || requestURI.contains("register")) {
            log.info("LoginInterceptor: Skipping authentication for login/register endpoint");
            return true;
        } else {
            // Get token from request header
            String token = request.getHeader("token");

            if (StringUtils.isEmpty(token)) {
                log.warn("LoginInterceptor: No token provided in request headers");
                // Continue processing - authentication will be handled by Spring Security
                return true;
            }

            // Get userId from Redis using token
            String userId = stringRedisTemplate.opsForValue().get(Constants.REDIS_TOEKN_KEY + token);

            if (StringUtils.isEmpty(userId)) {
                log.warn("LoginInterceptor: Invalid or expired token");
                // Continue processing - authentication will be handled by Spring Security
                return true;
            }

            log.info("LoginInterceptor: Valid token for userId: {}", userId);

            // Refresh token expiration time
            stringRedisTemplate.opsForValue().set(
                Constants.REDIS_TOEKN_KEY + token,
                userId,
                Constants.TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
            );

            // Set userId attribute in request for use in controllers
            request.setAttribute("userId", userId);
        }

        return true;
    }

}
