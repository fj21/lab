package com.cqu.lab.dao.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户信息表
 * @TableName user
 */
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户名
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码,md5加密
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 头像
     */
    private String image;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 0正常
   1锁定
     */
    private Integer status;

    /**
     * 关注数
     */
    private Integer attention;

    /**
     * 粉丝数
     */
    private Integer followers;

    /**
     * 创作数
     */
    private Integer works;

    /**
     * 获赞数
     */
    private Integer likes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}