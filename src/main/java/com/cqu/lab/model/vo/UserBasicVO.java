package com.cqu.lab.model.vo;

import lombok.Data;

/**
 * 用户基本信息视图对象（用于简略显示）
 */
@Data
public class UserBasicVO {
    
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