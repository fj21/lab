package com.cqu.lab.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cqu.lab.utils.ThreadLocalUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author jiang jinhong
 * @date 2025/1/29 8:45
 * @description token拦截器
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    /**
     * 请求前将请求中的 userId 存入到当前线程中
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 是否通过
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String userId = request.getHeader("userId");

        log.debug("TokenInterceptor: Processing request URI: {}", requestURI);
        log.debug("TokenInterceptor: UserId header: {}", userId);

        if (userId != null) {
            log.debug("TokenInterceptor: Setting userId in ThreadLocal: {}", userId);
            ThreadLocalUtil.setUserId(Integer.valueOf(userId));
        } else {
            log.debug("TokenInterceptor: No userId found in request headers");
            // Try to get userId from request attributes (set by LoginInterceptor)
            Object userIdAttr = request.getAttribute("userId");
            if (userIdAttr != null) {
                log.debug("TokenInterceptor: Using userId from request attributes: {}", userIdAttr);
                ThreadLocalUtil.setUserId(Integer.valueOf(userIdAttr.toString()));
            }
        }

        return true;
    }

    /**
     * 移除当前线程中的 userId 变量
     * @param request request
     * @param response response
     * @param handler 处理器
     * @param ex 异常
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        ThreadLocalUtil.clear();
    }

}
