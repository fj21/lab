package com.cqu.lab.model.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户信息更新数据传输对象
 */
@Data
public class UserUpdateDTO {

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 用户名
     */
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;

    /**
     * 密码
     */
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    /**
     * 用户角色（0-普通用户，1-管理员）
     */
    private Integer role;

    /**
     * 用户头像URL
     */
    private String image;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 用户状态（0-正常，1-禁用）
     */
    private Integer status;
}