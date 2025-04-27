package com.cqu.lab.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("user")
@Document(indexName = "#{@environment.getProperty('elasticsearch.user-index')}")
public class User {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户手机号，用于登录
     */
    @Field(type = FieldType.Keyword)
    private String phone;

    /**
     * 用户名
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String username;

    /**
     * 密码，MD5加密存储
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 用户头像URL
     */
    private String image;

    /**
     * 个性签名
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String signature;

    /**
     * 账号状态：0-正常，1-锁定
     */
    private Integer status;

    /**
     * 关注数
     */
    private Integer attention;

    /**
     * 粉丝数
     */
    private Integer followers;

    /**
     * 作品数
     */
    private Integer works;

    /**
     * 获赞数
     */
    private Integer likes;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}