package com.cqu.lab.model.common;

/**
 * 响应码枚举类
 */
public enum ResultCode implements IErrorCode {
    
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    
    /**
     * 操作失败
     */
    FAILED(500, "操作失败"),
    
    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(400, "参数检验失败"),
    
    /**
     * 暂未登录或token已经过期
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    
    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),
    
    /**
     * 手机号已被注册
     */
    PHONE_REGISTERED(1001, "手机号已被注册"),
    
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(1002, "用户不存在"),
    
    /**
     * 密码错误
     */
    PASSWORD_ERROR(1003, "密码错误"),
    
    /**
     * 账户已被锁定
     */
    ACCOUNT_LOCKED(1004, "账户已被锁定"),
    
    /**
     * 手机号格式错误
     */
    PHONE_FORMAT_ERROR(1005, "手机号格式错误");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    @Override
    public Integer getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
} 