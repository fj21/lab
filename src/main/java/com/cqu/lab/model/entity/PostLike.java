package com.cqu.lab.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户点赞表
 * @TableName post_like
 */
@Data
public class PostLike implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 发布帖子的用户
     */
    private Integer authorId;

    /**
     * 被点赞的帖子
     */
    private Long videoId;

    /**
     * 点赞的用户
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}