package com.cqu.lab.controller;

import com.cqu.lab.model.common.Result;
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
                                              @RequestParam(required = false, defaultValue = "0") Integer lastPostId){
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
     * @param type 类型 0-图片  1-视频
     * @return 是否成功
     */
    @PostMapping("/post")
    public Result<Boolean> post(@RequestParam Integer type,
                                @RequestParam Integer category,
                                @RequestParam Integer visibility,
                                @RequestParam String  content,
                                @RequestParam MultipartFile[] files
                                ){
        Boolean result = postService.post(type,category,visibility,content,files);
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
