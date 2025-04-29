package generator.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 资源表
 * @TableName resource
 */
@Data
public class Resource implements Serializable {
    /**
     * 资源ID
     */
    private Long id;

    /**
     * 上传者ID
     */
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
     * 是否删除 0-未删除 1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}