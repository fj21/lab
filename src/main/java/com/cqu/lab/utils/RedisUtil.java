package com.cqu.lab.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisUtil {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 缓存基本的对象
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间(秒)
     */
    public void setCacheObject(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
    
    /**
     * 缓存基本的对象，永不过期
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public void setCacheObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 获得缓存的基本对象
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public Object getCacheObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 删除缓存
     * @param key 缓存的键值
     */
    public boolean deleteObject(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
    
    /**
     * 设置有效时间
     * @param key 缓存的键值
     * @param timeout 超时时间(秒)
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }
    
    /**
     * 获取有效时间
     * @param key 缓存的键值
     * @return 有效时间
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
    
    /**
     * 判断key是否存在
     * @param key 缓存的键值
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
} 