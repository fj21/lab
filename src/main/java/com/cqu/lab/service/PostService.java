package com.cqu.lab.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cqu.lab.model.entity.Post;
import com.cqu.lab.model.vo.PostDetailVO;
import com.cqu.lab.model.vo.PostListVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Jinhong Jiang
* @description 针对表【post(帖子表)】的数据库操作Service
* @createDate 2025-04-12 15:54:03
*/
public interface PostService extends IService<Post> {

    /**
     * 返回帖子列表
     * @param category
     * @param lastPostId
     * @return
     */
    PostListVO getSectionPosts(Integer category, Integer lastPostId);

    /**
     * 返回帖子详情
     * @param postId
     * @return
     */
    PostDetailVO getPostDetail(Integer postId);

    /**
     * 发布帖子
     * @param type
     * @param category
     * @param visibility
     * @param files
     * @return
     */
    Boolean post(Integer type, Integer category, Integer visibility,String content, MultipartFile[] files);

    /**
     * 点赞帖子
     * @param postId postId
     * @return
     */
    Boolean doLikePost(Integer postId);

    /**
     * 收藏帖子
     * @param postId postId
     * @return
     */
    Boolean doCollectPost(Integer postId);

    /**
     * 获得对应的作品列表
     * @param authorId
     * @return
     */
    PostListVO getPersonPosts(Integer authorId, Integer lastPostId);
}
