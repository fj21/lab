package com.cqu.lab.constant;

/**
 * 系统常量类
 */
public class Constants {
    
    /**
     * Redis用户信息前缀
     */
    public static final String REDIS_USER_KEY_PREFIX = "lab:user:";
    
    /**
     * Redis用户基本信息前缀
     */
    public static final String REDIS_USER_BASIC_INFO_KEY_PREFIX = "lab:user:basic:";
    
    /**
     * 用户信息缓存过期时间（1小时）
     */
    public static final long USER_CACHE_EXPIRE_TIME = 3600L;
    
    /**
     * JWT令牌前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    
    /**
     * JWT存储的请求头
     */
    public static final String JWT_HEADER = "Authorization";
    
    /**
     * 用户更新消息主题
     */
    public static final String USER_UPDATE_TOPIC = "lab-user-update";
    
    /**
     * 用户手机号加锁前缀
     */
    public static final String USER_PHONE_LOCK_PREFIX = "lab:lock:user:phone:";
    
    /**
     * 用户手机号加锁等待时间（秒）
     */
    public static final long USER_PHONE_LOCK_WAIT_TIME = 3L;
    
    /**
     * 用户手机号加锁释放时间（秒）
     */
    public static final long USER_PHONE_LOCK_RELEASE_TIME = 10L;
    
    /**
     * 用户状态：正常
     */
    public static final int USER_STATUS_NORMAL = 0;
    
    /**
     * 用户状态：锁定
     */
    public static final int USER_STATUS_LOCKED = 1;
} 