package com.cqu.lab.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户角色表
 * @TableName user_role
 */
@Data
public class UserRole implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 角色 0-普通用户 1-管理员
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}