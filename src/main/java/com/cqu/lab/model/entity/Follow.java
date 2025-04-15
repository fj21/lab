package com.cqu.lab.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户关注表
 * @TableName follow
 */
@Data
public class Follow implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 被关注的用户
     */
    private Integer followId;

    /**
     * 操作关注用户
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}