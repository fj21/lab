package com.cqu.lab.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PostListVO implements Serializable {

    public Integer lastPostId;

    public List<PostVO> postVOList;

    @Data
    public static class PostVO implements Serializable{
        private static final long serialVersionUID = 2L;

        /**
         * 帖子ID
         */
        private Long id;

        /**
         * 帖子标题
         */
        private String title;

        /**
         * 作者简略信息
         */
        UserBasicVO userBasicVO;

        /**
         * 文字内容
         */
        private String content;


        /**
         * 内容类型
         */
        private Integer mediaType;

        /**
         * 帖子封面地址
         */
        private String coverUrl;

        /**
         * 点赞数
         */
        private Integer likeCount;

        /**
         * 创建时间
         */
        private Date createdAt;

        /**
         * 更新时间
         */
        private Date updatedAt;
    }

    private static final long serialVersionUID = 1L;
}
