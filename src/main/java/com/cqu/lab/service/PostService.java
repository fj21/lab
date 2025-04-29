package com.cqu.lab.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cqu.lab.model.dto.ContentUpdateDTO;
import com.cqu.lab.model.dto.PostCreateDTO;
import com.cqu.lab.model.entity.Post;
import com.cqu.lab.model.vo.PostDetailVO;
import com.cqu.lab.model.vo.PostListVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
* @author Jinhong Jiang
* @description 针对表【post(帖子表)】的数据库操作Service
* @createDate 2025-04-12 15:54:03
*/
public interface PostService extends IService<Post> {

    /**
     * 返回帖子列表
     * @param category 分类
     * @param lastPostId 上一页最后一条帖子ID
     * @return 帖子列表
     */
    PostListVO getSectionPosts(Integer category, Integer lastPostId);

    /**
     * 返回帖子详情
     * @param postId 帖子ID
     * @return 帖子详情
     */
    PostDetailVO getPostDetail(Integer postId);

    /**
     * 发布帖子
     * @param postCreateDTO 发布帖子dto
     * @return 是否成功
     */
    Boolean post(PostCreateDTO postCreateDTO,MultipartFile[] files);

    /**
     * 点赞帖子
     * @param postId 帖子ID
     * @return 是否成功
     */
    Boolean doLikePost(Integer postId);

    /**
     * 收藏帖子
     * @param postId 帖子ID
     * @return 是否成功
     */
    Boolean doCollectPost(Integer postId);

    /**
     * 获得对应的作品列表
     * @param authorId 作者ID
     * @param lastPostId 上一页最后一条帖子ID
     * @return 帖子列表
     */
    PostListVO getPersonPosts(Integer authorId, Integer lastPostId);

    /**
     * 根据内容类型获取内容
     * @param type 内容类型（research-研究方向，achievements-科研成果，team-团队介绍）
     * @return 内容数据
     */
    Map<String, Object> getContentByType(String type);

    /**
     * 更新内容
     * @param type 内容类型
     * @param contentUpdateDTO 内容更新DTO
     * @return 是否成功
     */
    Boolean updateContent(String type, ContentUpdateDTO contentUpdateDTO);
}
