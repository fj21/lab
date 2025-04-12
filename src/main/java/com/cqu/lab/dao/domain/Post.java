package com.cqu.lab.dao.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 帖子表
 * @TableName post
 */
@Data
public class Post implements Serializable {
    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 作者ID
     */
    private Long userId;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 内容类型
     */
    private Integer type;

    /**
     * 可见性 0 - public  1- private  2- friends-only
     */
    private Integer visibility;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 浏览数
     */
    private Integer viewCount;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}