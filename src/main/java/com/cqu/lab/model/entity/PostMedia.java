package com.cqu.lab.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 帖子资源表
 * @TableName post_media
 */
@Data
public class PostMedia implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 所属帖子
     */
    private Long postId;

    /**
     * 媒体类型  0-图片  1-视频
     */
    private Integer mediaType;

    /**
     * 资源地址
     */
    private String mediaUrl;

    /**
     * 视频封面图地址
     */
    private String coverUrl;

    /**
     * 排序序号
     */
    private Integer sortOrder;

    private static final long serialVersionUID = 1L;
}