package com.cqu.lab.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户登录数据传输对象
 */
@Data
public class UserLoginDTO {
    
    /**
     * 用户手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}