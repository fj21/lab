package com.cqu.lab.model.entity;

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
     * 分类 0-新闻 1-设备 2-师生 3-生活...
     */
    private Integer category;

    /**
     * 内容类型 0-图片  1-视频
     */
    private Integer mediaType;

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