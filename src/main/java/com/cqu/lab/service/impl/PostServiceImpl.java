package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cqu.lab.mapper.PostCollectMapper;
import com.cqu.lab.mapper.PostLikeMapper;
import com.cqu.lab.mapper.PostMediaMapper;
import com.cqu.lab.model.entity.Post;
import com.cqu.lab.model.entity.PostCollect;
import com.cqu.lab.model.entity.PostLike;
import com.cqu.lab.model.entity.PostMedia;
import com.cqu.lab.model.vo.PostDetailVO;
import com.cqu.lab.model.vo.PostListVO;
import com.cqu.lab.model.vo.UserBasicVO;
import com.cqu.lab.service.PostService;
import com.cqu.lab.mapper.PostMapper;
import com.cqu.lab.service.UserService;
import com.cqu.lab.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqu.lab.mapper.CommentMapper;
import com.cqu.lab.model.entity.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Jinhong Jiang
* @description 针对表【post(帖子表)】的数据库操作Service实现
* @createDate 2025-04-12 15:54:03
*/
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;
    private final PostCollectMapper postCollectMapper;
    private final PostMediaMapper postMediaMapper;
    private final CommentMapper commentMapper;
    private final UserService userService;
    /**
     * 返回帖子列表
     * @param category
     * @param lastPostId
     * @return
     */
    @Override
    public PostListVO getSectionPosts(Integer category,Integer lastPostId) {
        //1.根据category和lastPostId取50条数据
        if(lastPostId.equals(0)) lastPostId = postMapper.selectNewesPost();
        List<Post> posts = postMapper.selectSectionPost(category, lastPostId);

        if (posts == null || posts.isEmpty()) {
            return new PostListVO();
        }

        // 构建返回结果
        PostListVO postListVO = new PostListVO();
        postListVO.setLastPostId(posts.get(posts.size() - 1).getId().intValue());

        // 构建帖子列表
        List<PostListVO.PostVO> postVOList = new ArrayList<>();

        for (Post post : posts) {
            PostListVO.PostVO postVO = new PostListVO.PostVO();
            postVO.setId(post.getId());
            postVO.setContent(post.getContent());
            postVO.setMediaType(post.getMediaType());
            postVO.setLikeCount(post.getLikeCount());
            postVO.setCreatedAt(post.getCreatedAt());
            postVO.setUpdatedAt(post.getUpdatedAt());

            // 2.根据postId去PostMedia取coverUrl
            PostMedia postMedia = postMediaMapper.selectOne(
                new QueryWrapper<PostMedia>().eq("post_id", post.getId()).orderByAsc("sort_order").last("LIMIT 1")
            );
            if (postMedia != null) {
                postVO.setCoverUrl(postMedia.getCoverUrl());
            }

            // 3.根据作者id取作者basicInfo
            UserBasicVO userBasicVO = userService.getUserBasicInfo(post.getUserId().intValue());
            postVO.setUserBasicVO(userBasicVO);

            postVOList.add(postVO);
        }

        postListVO.setPostVOList(postVOList);
        return postListVO;
    }

    /**
     * 返回帖子详情
     * @param postId
     * @return
     */
    @Override
    public PostDetailVO getPostDetail(Integer postId) {
        //1.根据 postId 取 post 相关信息 和 postMedia 相关信息
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return null;
        }

        // 更新浏览数
        post.setViewCount(post.getViewCount() + 1);
        updateById(post);

        // 构建返回结果
        PostDetailVO postDetailVO = new PostDetailVO();
        postDetailVO.setId(post.getId());
        postDetailVO.setAuthorId(post.getUserId());
        postDetailVO.setContent(post.getContent());
        postDetailVO.setCategory(post.getCategory());
        postDetailVO.setMediaType(post.getMediaType());
        postDetailVO.setLikeCount(post.getLikeCount());
        postDetailVO.setCollectCount(post.getCollectCount());
        postDetailVO.setCommentCount(post.getCommentCount());
        postDetailVO.setCreatedAt(post.getCreatedAt());

        // 查询帖子媒体资源
        QueryWrapper<PostMedia> mediaQueryWrapper = new QueryWrapper<>();
        mediaQueryWrapper.eq("post_id", post.getId()).orderByAsc("sort_order");
        List<PostMedia> postMediaList = postMediaMapper.selectList(mediaQueryWrapper);

        // 设置封面图
        if (!postMediaList.isEmpty()) {
            PostMedia firstMedia = postMediaList.get(0);
            postDetailVO.setCoverUrl(firstMedia.getCoverUrl());
        }

        // 构建媒体列表
        List<PostDetailVO.MediaVO> mediaVOList = postMediaList.stream().map(media -> {
            PostDetailVO.MediaVO mediaVO = new PostDetailVO.MediaVO();
            mediaVO.setMediaUrl(media.getMediaUrl());
            mediaVO.setSortOrder(media.getSortOrder());
            return mediaVO;
        }).collect(Collectors.toList());
        postDetailVO.setMediaVOList(mediaVOList);

        //2.根据 postId 取相关 comment 信息10条
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("post_id", post.getId())
                         .eq("parent_id", 0) // 只查询一级评论
                         .orderByDesc("create_time")
                         .last("LIMIT 10");
        List<Comment> commentList = commentMapper.selectList(commentQueryWrapper);

        // 构建评论列表
        List<PostDetailVO.ParentCommentVO> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            PostDetailVO.ParentCommentVO commentVO = new PostDetailVO.ParentCommentVO();
            commentVO.setId(comment.getId());
            commentVO.setPostId(comment.getPostId());
            commentVO.setCommenterId(comment.getUserId());
            commentVO.setParentId(comment.getParentId());
            commentVO.setContent(comment.getContent());
            commentVO.setLikedNum(comment.getLikedNum());
            commentVO.setCreateTime(comment.getCreateTime());

            // 查询子评论数量
            QueryWrapper<Comment> childQueryWrapper = new QueryWrapper<>();
            childQueryWrapper.eq("parent_id", comment.getId());
            Long childCount = commentMapper.selectCount(childQueryWrapper);
            commentVO.setHasChildComment(childCount > 0);
            commentVO.setChildCommentNum(childCount.intValue());

            //3.根据 commentId 取对应评论人的基本信息
            UserBasicVO userBasicVO = userService.getUserBasicInfo(comment.getUserId());
            commentVO.setUserBasicVO(userBasicVO);

            commentVOList.add(commentVO);
        }
        postDetailVO.setCommentVOList(commentVOList);

        // 设置评论游标
        if (!commentList.isEmpty()) {
            postDetailVO.setLastCommentId(commentList.get(commentList.size() - 1).getId().intValue());
        }

        return postDetailVO;
    }

    /**
     * 发布帖子
     * @param type 类型 0-图片 1-视频
     * @param category 分区
     * @param visibility 可见性
     * @param files 文件列表
     * @return 是否发布成功
     */
    @Override
    public Boolean post(Integer type, Integer category, Integer visibility,String content, MultipartFile[] files) {
        // 参数校验
        if (files == null || files.length == 0) {
            return false;
        }

        // 如果是图片类型，限制最多18张
        if (type == 0 && files.length > 18) {
            return false;
        }

        // 创建帖子实体
        Post post = new Post();
        post.setUserId(Long.valueOf(ThreadLocalUtil.getUserId()));
        post.setContent(content); // 默认空内容，可以在前端添加文字内容
        post.setCategory(category);
        post.setMediaType(type);
        post.setVisibility(visibility);
        post.setLikeCount(0);
        post.setCollectCount(0);
        post.setCommentCount(0);
        post.setViewCount(0);
        post.setIsDeleted(0);
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());

        // 保存帖子
        boolean saved = save(post);
        if (!saved) {
            return false;
        }

        // 处理文件上传
        try {
            // 模拟文件上传逻辑，实际应该使用云存储服务
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];

                // 创建媒体资源实体
                PostMedia postMedia = new PostMedia();
                postMedia.setPostId(post.getId());
                postMedia.setMediaType(type);

                // 模拟文件上传到云存储，获取URL
                String mediaUrl = "https://example.com/media/" + System.currentTimeMillis() + "_" + i + "_" + file.getOriginalFilename();
                postMedia.setMediaUrl(mediaUrl);

                // 如果是视频类型，设置封面图
                if (type == 1) {
                    String coverUrl = "https://example.com/cover/" + System.currentTimeMillis() + "_" + file.getOriginalFilename() + ".jpg";
                    postMedia.setCoverUrl(coverUrl);
                }

                postMedia.setSortOrder(i);

                // 保存媒体资源
                postMediaMapper.insert(postMedia);
            }

            return true;
        } catch (Exception e) {
            // 如果文件处理失败，删除已创建的帖子
            removeById(post.getId());
            return false;
        }
    }

    /**
     * 点赞帖子
     * @param postId postId
     * @return
     */
    @Override
    public Boolean doLikePost(Integer postId) {
        //先在对应帖子的 redis set 中增加点赞的用户id
        //然后再发送消息异步更新 mysql 表
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return false;
        }

        // 更新帖子点赞数
        post.setLikeCount(post.getLikeCount()+1);
        updateById(post);

        // 创建点赞记录
        PostLike postLike = new PostLike();
        postLike.setVideoId(post.getId());
        postLike.setAuthorId(post.getUserId().intValue()); // 帖子作者ID
        postLike.setUserId(ThreadLocalUtil.getUserId()); // 当前用户ID
        postLike.setCreateTime(new Date());

        int insert = postLikeMapper.insert(postLike);
        return insert > 0;
    }

    /**
     * 收藏帖子
     * @param postId postId
     * @return
     */
    @Override
    public Boolean doCollectPost(Integer postId) {
        // 获取当前帖子信息
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return false;
        }

        // 更新帖子收藏数
        post.setCollectCount(post.getCollectCount() + 1);
        updateById(post);

        // 创建收藏记录
        PostCollect postCollect = new PostCollect();
        postCollect.setVideoId(post.getId());
        postCollect.setAuthorId(post.getUserId().intValue()); // 帖子作者ID
        postCollect.setUserId(ThreadLocalUtil.getUserId()); // 当前用户ID
        postCollect.setCreateTime(new Date());

        // 保存收藏记录
        int insert = postCollectMapper.insert(postCollect);
        return insert > 0;
    }

    @Override
    public PostListVO getPersonPosts(Integer authorId, Integer lastPostId) {
        //1.根据category和lastPostId取50条数据
        if(lastPostId.equals(0)) lastPostId = postMapper.selectNewesPost();
        if(authorId == null) authorId = ThreadLocalUtil.getUserId();

        // 查询用户发布的帖子
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", authorId)
                   .lt("id", lastPostId)
                   .orderByDesc("created_at")
                   .last("LIMIT 50");

        List<Post> posts = postMapper.selectList(queryWrapper);

        if (posts == null || posts.isEmpty()) {
            return new PostListVO();
        }

        // 构建返回结果
        PostListVO postListVO = new PostListVO();
        postListVO.setLastPostId(posts.get(posts.size() - 1).getId().intValue());

        // 构建帖子列表
        List<PostListVO.PostVO> postVOList = new ArrayList<>();

        for (Post post : posts) {
            PostListVO.PostVO postVO = new PostListVO.PostVO();
            postVO.setId(post.getId());
            postVO.setContent(post.getContent());
            postVO.setMediaType(post.getMediaType());
            postVO.setLikeCount(post.getLikeCount());
            postVO.setCreatedAt(post.getCreatedAt());
            postVO.setUpdatedAt(post.getUpdatedAt());

            // 2.根据postId去PostMedia取coverUrl
            PostMedia postMedia = postMediaMapper.selectOne(
                new QueryWrapper<PostMedia>().eq("post_id", post.getId()).orderByAsc("sort_order").last("LIMIT 1")
            );
            if (postMedia != null) {
                postVO.setCoverUrl(postMedia.getCoverUrl());
            }

            // 3.根据作者id取作者basicInfo
            UserBasicVO userBasicVO = userService.getUserBasicInfo(post.getUserId().intValue());
            postVO.setUserBasicVO(userBasicVO);

            postVOList.add(postVO);
        }

        postListVO.setPostVOList(postVOList);
        return postListVO;
    }
}




