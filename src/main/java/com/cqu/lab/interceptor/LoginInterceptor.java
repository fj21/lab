package com.cqu.lab.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cqu.lab.constant.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * 登录拦截器
 */
@Order(0)
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if(requestURI.contains("login")){
            return true;
        }else {
            String token = request.getHeader("token");
            String userId = stringRedisTemplate.opsForValue().get(Constants.REDIS_TOEKN_KEY + token);
            if(!StringUtils.isEmpty(userId)){
                //刷新token时间
                stringRedisTemplate.opsForValue().set(Constants.REDIS_TOEKN_KEY+token,userId,
                        Constants.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
                request.setAttribute("userId",userId);
            }
        }
        return true;
    }

}
