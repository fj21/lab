package com.cqu.lab.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 */
@Configuration
public class RedissonConfig {
    
    @Value("${redisson.single-server-config.address}")
    private String address;
    
    @Value("${redisson.single-server-config.password}")
    private String password;
    
    @Value("${redisson.single-server-config.database}")
    private int database;
    
    /**
     * 配置Redisson客户端
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(database);
        
        if (password != null && !password.isEmpty()) {
            config.useSingleServer().setPassword(password);
        }
        
        return Redisson.create(config);
    }
} 