package com.cqu.lab.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源表
 * @TableName resource
 */
@Data
@TableName("resource")
public class Resource implements Serializable {
    /**
     * 资源ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上传者ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源内容
     */
    private String content;

    /**
     * 资源类型 1-书籍 2-实验指南 3-视频教程 4-软件工具
     */
    private Integer type;

    /**
     * 封面图URL
     */
    @TableField("cover_url")
    private String coverUrl;

    /**
     * 资源文件URL
     */
    @TableField("download_url")
    private String downloadUrl;

    /**
     * 下载次数
     */
    @TableField("download_count")
    private Integer downloadCount;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}
