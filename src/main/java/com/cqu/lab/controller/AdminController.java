package com.cqu.lab.controller;

import com.cqu.lab.model.common.Result;
import com.cqu.lab.model.dto.ContentUpdateDTO;
import com.cqu.lab.model.dto.SystemSettingDTO;
import com.cqu.lab.model.dto.UserCreateDTO;
import com.cqu.lab.model.dto.UserUpdateDTO;
import com.cqu.lab.model.entity.Post;
import com.cqu.lab.model.entity.User;
import com.cqu.lab.model.vo.PostListVO;
import com.cqu.lab.model.vo.ResourceListVO;
import com.cqu.lab.model.vo.ResourceVO;
import com.cqu.lab.model.vo.StatisticsVO;
import com.cqu.lab.model.vo.SystemSettingVO;
import com.cqu.lab.model.vo.UserListVO;
import com.cqu.lab.service.PostService;
import com.cqu.lab.service.ResourceService;
import com.cqu.lab.service.UserService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理后台控制器
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminController {

    private final PostService postService;
    private final UserService userService;
    private final ResourceService resourceService;

    /**
     * 获取所有帖子（分页）
     * @param category 分类
     * @param lastPostId 上一页最后一条帖子ID
     * @return 帖子列表
     */
    @GetMapping("/posts")
    public Result<PostListVO> getAllPosts(
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false, defaultValue = "0") Integer lastPostId) {
        PostListVO postListVO = postService.getSectionPosts(category, lastPostId);
        return Result.success(postListVO);
    }

    /**
     * 删除帖子
     * @param postId 帖子ID
     * @return 是否成功
     */
    @DeleteMapping("/post/{postId}")
    public Result<Boolean> deletePost(@PathVariable Integer postId) {
        Boolean result = postService.removeById(postId);
        return Result.success(result);
    }

    /**
     * 获取所有用户（分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户列表
     */
    @GetMapping("/users")
    public Result<UserListVO> getAllUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        UserListVO userListVO = userService.getUserList(pageNum, pageSize);
        return Result.success(userListVO);
    }

    /**
     * 创建用户
     * @param userCreateDTO 用户创建DTO
     * @return 用户ID
     */
    @PostMapping("/user")
    public Result<Integer> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        Integer userId = userService.createUser(userCreateDTO);
        return Result.success(userId);
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param userUpdateDTO 用户更新DTO
     * @return 是否成功
     */
    @PutMapping("/user/{userId}")
    public Result<Boolean> updateUser(
            @PathVariable Integer userId,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        Boolean result = userService.updateUserAdmin(userId, userUpdateDTO);
        return Result.success(result);
    }

    /**
     * 修改用户状态（启用/禁用）
     * @param userId 用户ID
     * @param status 状态（0-正常，1-禁用）
     * @return 是否成功
     */
    @PutMapping("/user/{userId}/status")
    public Result<Boolean> updateUserStatus(
            @PathVariable Integer userId,
            @RequestParam Integer status) {
        Boolean result = userService.updateUserStatus(userId, status);
        return Result.success(result);
    }

    /**
     * 获取内容管理数据
     * @param type 内容类型（research-研究方向，achievements-科研成果，team-团队介绍）
     * @return 内容数据
     */
    @GetMapping("/content/{type}")
    public Result<Map<String, Object>> getContent(@PathVariable String type) {
        Map<String, Object> content = postService.getContentByType(type);
        return Result.success(content);
    }

    /**
     * 更新内容
     * @param type 内容类型
     * @param contentUpdateDTO 内容更新DTO
     * @return 是否成功
     */
    @PutMapping("/content/{type}")
    public Result<Boolean> updateContent(
            @PathVariable String type,
            @Valid @RequestBody ContentUpdateDTO contentUpdateDTO) {
        Boolean result = postService.updateContent(type, contentUpdateDTO);
        return Result.success(result);
    }

    /**
     * 获取系统设置
     * @return 系统设置
     */
    @GetMapping("/settings")
    public Result<SystemSettingVO> getSystemSettings() {
        SystemSettingVO systemSettingVO = new SystemSettingVO();
        // 这里应该从配置服务获取系统设置
        systemSettingVO.setSiteName("实验室官网");
        systemSettingVO.setContactEmail("lab@example.com");
        systemSettingVO.setContactPhone("123456789");
        systemSettingVO.setAllowRegistration(true);
        return Result.success(systemSettingVO);
    }

    /**
     * 更新系统设置
     * @param systemSettingDTO 系统设置DTO
     * @return 是否成功
     */
    @PutMapping("/settings")
    public Result<Boolean> updateSystemSettings(@Valid @RequestBody SystemSettingDTO systemSettingDTO) {
        // 这里应该调用配置服务更新系统设置
        log.info("更新系统设置: {}", systemSettingDTO);
        return Result.success(true);
    }

    /**
     * 获取所有资源（分页）
     * @param type 资源类型
     * @param lastResourceId 上一页最后一条资源ID
     * @return 资源列表
     */
    @GetMapping("/resources")
    public Result<ResourceListVO> getAllResources(
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false, defaultValue = "0") Long lastResourceId) {
        ResourceListVO resourceListVO = resourceService.getResourceList(type, lastResourceId);
        return Result.success(resourceListVO);
    }

    /**
     * 获取资源详情
     * @param resourceId 资源ID
     * @return 资源详情
     */
    @GetMapping("/resource/{resourceId}")
    public Result<ResourceVO> getResourceDetail(@PathVariable Long resourceId) {
        ResourceVO resourceVO = resourceService.getResourceDetail(resourceId);
        return Result.success(resourceVO);
    }

    /**
     * 更新资源
     * @param resourceId 资源ID
     * @param type 资源类型
     * @param title 标题
     * @param description 描述
     * @param content 内容
     * @return 是否成功
     */
    @PutMapping("/resource/{resourceId}")
    public Result<Boolean> updateResource(
            @PathVariable Long resourceId,
            @RequestParam Integer type,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String content) {
        Boolean result = resourceService.updateResource(resourceId, type, title, description, content);
        return Result.success(result);
    }

    /**
     * 删除资源
     * @param resourceId 资源ID
     * @return 是否成功
     */
    @DeleteMapping("/resource/{resourceId}")
    public Result<Boolean> deleteResource(@PathVariable Long resourceId) {
        Boolean result = resourceService.deleteResource(resourceId);
        return Result.success(result);
    }

    /**
     * 获取统计数据
     * @return 统计数据
     */
    @GetMapping("/statistics")
    public Result<StatisticsVO> getStatistics() {
        StatisticsVO statisticsVO = new StatisticsVO();

        // 获取用户总数
        long userCount = userService.count();
        statisticsVO.setUserCount((int) userCount);

        // 获取帖子总数
        long postCount = postService.count();
        statisticsVO.setPostCount((int) postCount);

        // 获取今日新增用户数和帖子数
        // 这里应该从服务中获取实际数据
        statisticsVO.setTodayNewUsers(5);
        statisticsVO.setTodayNewPosts(10);

        return Result.success(statisticsVO);
    }
}
