package com.cqu.lab.dao.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 私信信息表
 * @TableName private_message
 */
@Data
public class PrivateMessage implements Serializable {
    /**
     * 主键Id
     */
    private Long id;

    /**
     * 发送者id
     */
    private Long senderId;

    /**
     * 接受者Id
     */
    private Long receiverId;

    /**
     * 消息类型,0：普通消息 1：朋友分享 2：系统消息
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 消息状态：0正常    1删除
     */
    private Integer status;

    /**
     * 发送时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}