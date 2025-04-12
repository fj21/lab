package com.cqu.lab.filter;

import com.cqu.lab.constant.Constants;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.service.UserService;
import com.cqu.lab.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Resource
    private UserService userService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        
        // 放行登录注册接口
        if (requestURI.contains("/api/user/login") || requestURI.contains("/api/user/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 获取JWT
        String jwt = getJwtFromRequest(request);
        
        if (StringUtils.hasText(jwt) && !jwtUtil.isTokenExpired(jwt)) {
            // 从JWT中获取用户ID
            Integer userId = jwtUtil.getUserIdFromToken(jwt);
            
            // 获取用户信息
            User user = userService.getById(userId);
            
            if (user != null && Constants.USER_STATUS_NORMAL == user.getStatus()) {
                // 创建认证信息
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(user.getId().toString())
                        .password("")
                        .authorities(new ArrayList<>())
                        .build();
                
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 将认证信息设置到SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 从请求中获取JWT
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.JWT_HEADER);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.JWT_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
}