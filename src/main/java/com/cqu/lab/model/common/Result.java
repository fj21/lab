package com.cqu.lab.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 返回消息
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T data;
    
    public Result() {
    }
    
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功返回结果
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }
    
    /**
     * 成功返回结果
     * @param data 获取的数据
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功返回结果
     * @param data 获取的数据
     * @param message 提示信息
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }
    
    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(IErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
    
    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 错误信息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(IErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }
    
    /**
     * 失败返回结果
     * @param message 提示信息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.FAILED.getCode(), message, null);
    }
    
    /**
     * 失败返回结果
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> failed() {
        return failed(ResultCode.FAILED);
    }
    
    /**
     * 参数验证失败返回结果
     * @param <T> 数据类型
     * @return 参数验证失败结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }
    
    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     * @param <T> 数据类型
     * @return 参数验证失败结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }
    
    /**
     * 未登录返回结果
     * @param <T> 数据类型
     * @return 未登录结果
     */
    public static <T> Result<T> unauthorized() {
        return failed(ResultCode.UNAUTHORIZED);
    }
    
    /**
     * 未授权返回结果
     * @param <T> 数据类型
     * @return 未授权结果
     */
    public static <T> Result<T> forbidden() {
        return failed(ResultCode.FORBIDDEN);
    }
} 