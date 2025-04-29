package com.cqu.lab.model.vo;

import com.cqu.lab.model.entity.PostMedia;

import lombok.Data;

import javax.xml.stream.events.Comment;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PostDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 作者ID, 即 postMedia 里面的 userId
     */
    private Long authorId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 分类 0-新闻 1-设备 2-师生 3-生活...
     */
    private Integer category;

    /**
     * 内容类型
     */
    private Integer mediaType;

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
     *
     */
    private Date createdAt;


    /**
     * 视频封面图地址
     */
    private String coverUrl;

    /**
     * 资源列表
     */
    private List<MediaVO> mediaVOList;

    /**
     * 一级评论列表
     */
    private List<ParentCommentVO> commentVOList;

    /**
     * 一级评论id的游标
     */
    private Integer lastCommentId;

    @Data
    public static class MediaVO implements Serializable{
        private static final long serialVersionUID = 1L;
        /**
         * 资源地址
         */
        private String mediaUrl;

        /**
         * 排序序号
         */
        private Integer sortOrder;
    }
    @Data
    public static class ParentCommentVO implements Serializable{
        private static final long serialVersionUID = 1L;

        /**
         * 评论人的基本信息
         */
        private UserBasicVO userBasicVO;

        /**
         * 评论id
         */
        private Long id;

        /**
         * 是否有子评论
         */
        private boolean hasChildComment;

        /**
         * 子评论个数
         */
        private Integer childCommentNum;

        /**
         * 被评论的视频
         */
        private Long postId;

        /**
         * 发表评论的用户
         */
        private Integer commenterId;

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
         * 创建时间
         */
        private Date createTime;
    }
}
