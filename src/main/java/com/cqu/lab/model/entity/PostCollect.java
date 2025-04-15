package com.cqu.lab.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户收藏表
 * @TableName post_collect
 */
@Data
public class PostCollect implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 发布视频的用户
     */
    private Integer authorId;

    /**
     * 被收藏的视频
     */
    private Long videoId;

    /**
     * 收藏的用户
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}