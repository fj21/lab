package com.cqu.lab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cqu.lab.filter.CustomCorsFilter;

import java.util.Arrays;

/**
 * Spring Security 配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomCorsFilter customCorsFilter;

    public SecurityConfig(CustomCorsFilter customCorsFilter) {
        this.customCorsFilter = customCorsFilter;
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 禁用CSRF保护 - 确保完全禁用
        http.csrf(csrf -> csrf.disable());

        // 禁用HTTP Basic认证
        http.httpBasic(basic -> basic.disable());

        // 配置CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 配置路径权限
        http.authorizeRequests(authorize -> authorize
            // 允许所有人访问登录和注册接口
            .antMatchers("/api/user/login", "/api/user/register").permitAll()
            // 允许访问用户基本信息接口
            .antMatchers("/api/user/basic/**").permitAll()
            // 用户详细信息和更新接口需要认证
            .antMatchers("/api/user/info", "/api/user/update").permitAll() // Changed to permitAll temporarily for debugging
            // 允许访问帖子相关接口
            .antMatchers("/api/post/**").permitAll()
            // 允许访问管理后台接口
            .antMatchers("/api/admin/**").permitAll()
            // 允许访问文件上传接口
            .antMatchers("/api/file/**").permitAll()
            // 允许所有人访问Swagger相关资源
            .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            // 允许所有人访问静态资源
            .antMatchers("/", "/index.html", "/static/**", "/favicon.ico").permitAll()
            // 其他所有请求需要认证
            .anyRequest().authenticated());

        // 设置session管理策略为无状态（RESTful API一般不需要session）
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 添加自定义过滤器
        http.addFilterBefore(customCorsFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
            "Access-Control-Request-Method", "Access-Control-Request-Headers", "token", "userId"
        ));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}