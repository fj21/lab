package com.cqu.lab.controller;

import cn.hutool.json.JSONUtil;
import com.cqu.lab.model.common.Result;
import com.cqu.lab.model.dto.PostCreateDTO;
import com.cqu.lab.model.entity.Post;
import com.cqu.lab.model.vo.PostDetailVO;
import com.cqu.lab.model.vo.PostListVO;
import com.cqu.lab.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;

    /**
     * 查询帖子列表
     * @param category
     * @param lastPostId
     * @return
     */
    @GetMapping("/section")
    public Result<PostListVO> getSectionPosts(@RequestParam(required = false) Integer category,
                                              @RequestParam(defaultValue = "0") Integer lastPostId){
        log.info("获取帖子列表，section:{},lastPostId:{}",category,lastPostId);
        PostListVO sectionPostList = postService.getSectionPosts(category,lastPostId);
        return Result.success(sectionPostList);
    }

    /**
     * 查询帖子详情
     * @param postId
     * @return
     */
    @GetMapping("/postDetail")
    public Result<PostDetailVO> getPostDetail(@RequestParam Integer postId){
        PostDetailVO postDetailVO = postService.getPostDetail(postId);
        return Result.success(postDetailVO);
    }

    /**
     * 发布帖子（视频，图文）
     * @param postCreateDTO 帖子DTO
     * @param files 文件列表
     * @return
     */
    @PostMapping("/post")
    public Result<Boolean> post(@RequestPart PostCreateDTO postCreateDTO,
                                @RequestPart(value = "files",required = false) MultipartFile[] files){
        // Handle null files array
        log.info("发布帖子，postCreateDTO:{}", JSONUtil.toJsonStr(postCreateDTO));
        MultipartFile[] safeFiles = files != null ? files : new MultipartFile[0];
        Boolean result = postService.post(postCreateDTO,safeFiles);
        return Result.success(result);
    }

    /**
     * 点赞帖子
     * @param postId
     * @return
     */
    @PostMapping("/doLikePost")
    public Result<Boolean> doLikePost(@RequestParam Integer postId){
        Boolean result = postService.doLikePost(postId);
        return  Result.success(result);
    }

    /**
     * 收藏帖子
     * @param postId
     * @return
     */
    @PostMapping("/doCollectPost")
    public Result<Boolean> doCollectPost(@RequestParam Integer postId){
        Boolean result = postService.doCollectPost(postId);
        return  Result.success(result);
    }

    @GetMapping("/personPosts")
    public Result<PostListVO> getPersonPosts(@RequestParam(required = false) Integer authorId,
                                             @RequestParam(required = false) Integer lastPostId){
        PostListVO postListVO = postService.getPersonPosts(authorId,lastPostId);
        return Result.success(postListVO);
    }
}
