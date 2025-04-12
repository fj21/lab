package com.cqu.lab.model.dto;

import lombok.Data;

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
     * 用户名
     */
    private String username;
    
    /**
     * 用户头像URL
     */
    private String image;
    
    /**
     * 个性签名
     */
    private String signature;
} 