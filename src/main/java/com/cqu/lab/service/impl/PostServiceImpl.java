package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cqu.lab.mapper.PostCollectMapper;
import com.cqu.lab.mapper.PostLikeMapper;
import com.cqu.lab.mapper.PostMediaMapper;
import com.cqu.lab.model.dto.ContentUpdateDTO;
import com.cqu.lab.model.dto.PostCreateDTO;
import com.cqu.lab.model.entity.Post;
import com.cqu.lab.model.entity.PostCollect;
import com.cqu.lab.model.entity.PostLike;
import com.cqu.lab.model.entity.PostMedia;
import com.cqu.lab.model.vo.PostDetailVO;
import com.cqu.lab.model.vo.PostListVO;
import com.cqu.lab.model.vo.UserBasicVO;
import com.cqu.lab.service.OssService;
import com.cqu.lab.service.PostService;
import com.cqu.lab.mapper.PostMapper;
import com.cqu.lab.service.UserService;
import com.cqu.lab.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqu.lab.mapper.CommentMapper;
import com.cqu.lab.model.entity.Comment;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Jinhong Jiang
* @description 针对表【post(帖子表)】的数据库操作Service实现
* @createDate 2025-04-12 15:54:03
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;
    private final PostCollectMapper postCollectMapper;
    private final PostMediaMapper postMediaMapper;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final OssService ossService;
    /**
     * 返回帖子列表
     * @param category
     * @param lastPostId
     * @return
     */
    @Override
    public PostListVO getSectionPosts(Integer category,Integer lastPostId) {
        //1.根据category和lastPostId取50条数据
        //如果category为空，则默认查询全部的数据
        if(lastPostId.equals(0)) lastPostId = postMapper.selectNewestPost();
        List<Post> posts = new ArrayList<>();
        if(category == null){
            posts = postMapper.selectSectionPostWithoutCategory(lastPostId);
        }else {
            posts = postMapper.selectSectionPost(category, lastPostId);
        }


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
            postVO.setTitle(post.getTitle());
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
        postDetailVO.setTitle(post.getTitle());
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
     * @param postCreateDTO 发布帖子dto
     * @return
     */
    @Override
    public Boolean post(PostCreateDTO postCreateDTO,MultipartFile[] files) {
        Integer type = postCreateDTO.getType();
        String title = postCreateDTO.getTitle();
        Integer category = postCreateDTO.getCategory();
        Integer visibility = postCreateDTO.getVisibility();
        String content = postCreateDTO.getContent();
        // 参数校验
//        if (files == null || files.length == 0) {
//            return false;
//        }

        // 如果是图片类型，限制最多18张
        if (type == 0 && files.length > 18) {
            return false;
        }

        // 创建帖子实体
        Post post = new Post();
        post.setUserId(Long.valueOf(ThreadLocalUtil.getUserId()));
        post.setTitle(title);
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
        int insert = postMapper.insert(post);
        if (insert == 0) {
            return false;
        }
        log.info("保存帖子成功，postid：{}",post.getId());
        // 处理文件上传
        try {
            log.info("Processing {} files for post ID: {}", files.length, post.getId());

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];

                // 创建媒体资源实体
                PostMedia postMedia = new PostMedia();
                postMedia.setPostId(post.getId());
                postMedia.setMediaType(type);
                postMedia.setSortOrder(i);

                // 根据类型上传到不同目录
                String mediaUrl;
                String coverUrl = null;

                if (type == 0) { // 图片类型
                    // 上传图片到OSS
                    mediaUrl = ossService.uploadImage(file);
                    log.info("Uploaded image: {}", mediaUrl);

                    // 对于图片，第一张作为封面
                    if (i == 0) {
                        coverUrl = mediaUrl;
                    }
                } else { // 视频类型
                    // 上传视频到OSS
                    mediaUrl = ossService.uploadVideo(file);
                    log.info("Uploaded video: {}", mediaUrl);

                    // 为视频生成封面图（这里简化处理，实际应该提取视频帧或让用户上传封面）
                    // 这里我们假设用户上传了视频封面作为第二个文件
                    if (i < files.length - 1 && files[i+1].getContentType().startsWith("image/")) {
                        coverUrl = ossService.uploadCover(files[i+1]);
                        i++; // 跳过下一个文件，因为它是封面
                    } else {
                        // 如果没有提供封面，可以使用默认封面或生成一个
                        coverUrl = "https://cqulab.oss-cn-chengdu.aliyuncs.com/default/video_cover.jpg";
                    }
                }

                // 设置媒体URL和封面URL
                postMedia.setMediaUrl(mediaUrl);
                //仅把封面保存在sortOrder为0的postmedia中，避免资源浪费
                if(i == 0) postMedia.setCoverUrl(coverUrl);

                // 保存媒体资源
                postMediaMapper.insert(postMedia);
                log.info("Saved media record for post ID: {}, media URL: {}", post.getId(), mediaUrl);
            }

            return true;
        } catch (Exception e) {
            log.error("Failed to process files for post ID: " + post.getId(), e);

            // 如果文件处理失败，删除已创建的帖子
            removeById(post.getId());

            // 清理已上传的文件（如果有）
            // 这里可以添加清理逻辑，但需要记录已上传的文件URL

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
        if(lastPostId.equals(0)) lastPostId = postMapper.selectNewestPost();
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

    /**
     * 根据内容类型获取内容
     * @param type 内容类型（research-研究方向，achievements-科研成果，team-团队介绍）
     * @return 内容数据
     */
    @Override
    public Map<String, Object> getContentByType(String type) {
        Map<String, Object> result = new HashMap<>();

        // 根据类型查询对应的内容
        // 这里假设每种类型的内容都存储在Post表中，使用特定的category值来区分
        Integer category = getContentCategory(type);

        if (category != null) {
            // 查询该类型的最新内容
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category", category)
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at")
                    .last("LIMIT 1");

            Post post = postMapper.selectOne(queryWrapper);

            if (post != null) {
                // 解析内容
                String content = post.getContent();
                String title = "";
                String body = "";

                // 假设内容的第一行是标题，其余是正文
                if (content.contains("\n")) {
                    int firstLineEnd = content.indexOf("\n");
                    title = content.substring(0, firstLineEnd).trim();
                    body = content.substring(firstLineEnd + 1).trim();
                } else {
                    title = content;
                }

                result.put("id", post.getId());
                result.put("title", title);
                result.put("content", body);

                // 查询相关的媒体资源
                QueryWrapper<PostMedia> mediaQueryWrapper = new QueryWrapper<>();
                mediaQueryWrapper.eq("post_id", post.getId())
                        .orderByAsc("sort_order");

                List<PostMedia> mediaList = postMediaMapper.selectList(mediaQueryWrapper);
                List<String> imageUrls = mediaList.stream()
                        .map(PostMedia::getMediaUrl)
                        .collect(Collectors.toList());

                result.put("imageUrls", imageUrls);
            } else {
                // 如果没有找到内容，返回默认值
                result.put("id", null);
                result.put("title", "暂无内容");
                result.put("content", "暂无内容");
                result.put("imageUrls", new ArrayList<>());
            }
        } else {
            // 无效的类型
            result.put("error", "无效的内容类型");
        }

        return result;
    }

    /**
     * 更新内容
     * @param type 内容类型
     * @param contentUpdateDTO 内容更新DTO
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateContent(String type, ContentUpdateDTO contentUpdateDTO) {
        // 获取内容类型对应的分类ID
        Integer category = getContentCategory(type);

        if (category == null) {
            throw new RuntimeException("无效的内容类型");
        }

        // 查询该类型的最新内容
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category)
                .eq("is_deleted", 0)
                .orderByDesc("created_at")
                .last("LIMIT 1");

        Post existingPost = postMapper.selectOne(queryWrapper);

        // 构建完整内容（标题 + 正文）
        String fullContent = contentUpdateDTO.getTitle() + "\n\n" + contentUpdateDTO.getContent();

        if (existingPost != null) {
            // 更新现有内容
            Post updatePost = new Post();
            updatePost.setId(existingPost.getId());
            updatePost.setContent(fullContent);
            updatePost.setUpdatedAt(new Date());

            boolean updated = updateById(updatePost);

            if (updated) {
                // 处理图片
                if (contentUpdateDTO.getImageUrls() != null && !contentUpdateDTO.getImageUrls().isEmpty()) {
                    // 先删除现有的媒体资源
                    postMediaMapper.delete(new QueryWrapper<PostMedia>().eq("post_id", existingPost.getId()));

                    // 添加新的媒体资源
                    int sortOrder = 0;
                    for (String imageUrl : contentUpdateDTO.getImageUrls()) {
                        PostMedia postMedia = new PostMedia();
                        postMedia.setPostId(existingPost.getId());
                        postMedia.setMediaType(0); // 图片类型
                        postMedia.setMediaUrl(imageUrl);
                        postMedia.setCoverUrl(sortOrder == 0 ? imageUrl : null); // 第一张图作为封面
                        postMedia.setSortOrder(sortOrder++);

                        postMediaMapper.insert(postMedia);
                    }
                }

                return true;
            }

            return false;
        } else {
            // 创建新内容
            Post newPost = new Post();
            newPost.setUserId(Long.valueOf(ThreadLocalUtil.getUserId()));
            newPost.setContent(fullContent);
            newPost.setCategory(category);
            newPost.setMediaType(0); // 图片类型
            newPost.setVisibility(0); // 公开
            newPost.setLikeCount(0);
            newPost.setCollectCount(0);
            newPost.setCommentCount(0);
            newPost.setViewCount(0);
            newPost.setIsDeleted(0);
            newPost.setCreatedAt(new Date());
            newPost.setUpdatedAt(new Date());

            boolean saved = save(newPost);

            if (saved && contentUpdateDTO.getImageUrls() != null && !contentUpdateDTO.getImageUrls().isEmpty()) {
                // 添加媒体资源
                int sortOrder = 0;
                for (String imageUrl : contentUpdateDTO.getImageUrls()) {
                    PostMedia postMedia = new PostMedia();
                    postMedia.setPostId(newPost.getId());
                    postMedia.setMediaType(0); // 图片类型
                    postMedia.setMediaUrl(imageUrl);
                    postMedia.setCoverUrl(sortOrder == 0 ? imageUrl : null); // 第一张图作为封面
                    postMedia.setSortOrder(sortOrder++);

                    postMediaMapper.insert(postMedia);
                }
            }

            return saved;
        }
    }

    /**
     * 根据内容类型获取对应的分类ID
     * @param type 内容类型
     * @return 分类ID
     */
    private Integer getContentCategory(String type) {
        switch (type) {
            case "research":
                return 10; // 研究方向对应的分类ID
            case "achievements":
                return 11; // 科研成果对应的分类ID
            case "team":
                return 12; // 团队介绍对应的分类ID
            default:
                return null;
        }
    }
}




