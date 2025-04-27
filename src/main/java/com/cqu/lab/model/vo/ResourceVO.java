package com.cqu.lab.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源VO
 */
@Data
public class ResourceVO implements Serializable {
    /**
     * 资源ID
     */
    private Long id;

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
    private String coverUrl;

    /**
     * 资源文件URL
     */
    private String downloadUrl;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 上传者信息
     */
    private UserBasicVO userBasicVO;

    private static final long serialVersionUID = 1L;
}
