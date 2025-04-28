package com.cqu.lab.handler;

import com.cqu.lab.model.common.Result;
import com.cqu.lab.model.common.ResultCode;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.messaging.converter.MessageConversionException;
import org.apache.rocketmq.client.exception.MQClientException;

import java.util.Set;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常：{}", e.getMessage(), e);
        return Result.failed(e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage()).append("; ");
        }

        String errorMsg = sb.toString();
        log.warn("参数校验失败: {}", errorMsg);
        return Result.validateFailed(errorMsg);
    }

    /**
     * 处理Bean校验异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage()).append("; ");
        }

        String errorMsg = sb.toString();
        log.warn("参数绑定失败: {}", errorMsg);
        return Result.validateFailed(errorMsg);
    }

    /**
     * 处理单个参数校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<?> violation : violations) {
            sb.append(violation.getMessage()).append("; ");
        }

        String errorMsg = sb.toString();
        log.warn("参数验证失败: {}", errorMsg);
        return Result.validateFailed(errorMsg);
    }

    /**
     * 处理所有未处理的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.failed("系统异常，请联系管理员");
    }

    /**
     * RocketMQ 消息转换异常处理
     */
    @ExceptionHandler(MessageConversionException.class)
    public Result<Object> handleMessageConversionException(MessageConversionException e) {
        log.error("消息转换异常：{}", e.getMessage(), e);
        return Result.failed("消息转换失败，请检查消息格式");
    }

    /**
     * RocketMQ 客户端异常处理
     */
    @ExceptionHandler(MQClientException.class)
    public Result<Object> handleMQClientException(MQClientException e) {
        log.error("RocketMQ客户端异常：{}", e.getMessage(), e);
        // 消息服务异常不应影响用户操作，返回默认成功
        return Result.success(null, "操作已接收");
    }
}