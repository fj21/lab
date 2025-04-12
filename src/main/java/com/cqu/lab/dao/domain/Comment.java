package com.cqu.lab.dao.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评论表
 * @TableName comment
 */
@Data
public class Comment implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 被评论的视频
     */
    private Long postId;

    /**
     * 发表评论的用户
     */
    private Integer userId;

    /**
     * 关联的1级评论id，如果是一级评论，则值为0
     */
    private Long parentId;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Long likedNum;

    /**
     * 0正常
   1删除不可见
     */
    private Integer status;

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