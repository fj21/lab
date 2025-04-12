package com.cqu.lab.model.vo;

import lombok.Data;

/**
 * 用户视图对象
 */
@Data
public class UserVO {
    
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
    
    /**
     * 关注数
     */
    private Integer attention;
    
    /**
     * 粉丝数
     */
    private Integer followers;
    
    /**
     * 作品数
     */
    private Integer works;
    
    /**
     * 获赞数
     */
    private Integer likes;
} 