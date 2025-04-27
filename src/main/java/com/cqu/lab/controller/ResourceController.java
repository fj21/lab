package com.cqu.lab.controller;

import com.cqu.lab.model.common.Result;
import com.cqu.lab.model.vo.ResourceListVO;
import com.cqu.lab.model.vo.ResourceVO;
import com.cqu.lab.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源控制器
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/resource")
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * 获取资源列表
     * @param type 资源类型
     * @param lastResourceId 上一页最后一条资源ID
     * @return 资源列表
     */
    @GetMapping("/list")
    public Result<ResourceListVO> getResourceList(
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
    @GetMapping("/detail")
    public Result<ResourceVO> getResourceDetail(@RequestParam Long resourceId) {
        ResourceVO resourceVO = resourceService.getResourceDetail(resourceId);
        return Result.success(resourceVO);
    }

    /**
     * 上传资源
     * @param type 资源类型
     * @param title 标题
     * @param description 描述
     * @param content 内容
     * @param coverFile 封面文件
     * @param resourceFile 资源文件
     * @return 是否成功
     */
    @PostMapping("/upload")
    public Result<Boolean> uploadResource(
            @RequestParam Integer type,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile coverFile,
            @RequestParam(required = false) MultipartFile resourceFile) {
        Boolean result = resourceService.uploadResource(type, title, description, content, coverFile, resourceFile);
        return Result.success(result);
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
    @PutMapping("/{resourceId}")
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
    @DeleteMapping("/{resourceId}")
    public Result<Boolean> deleteResource(@PathVariable Long resourceId) {
        Boolean result = resourceService.deleteResource(resourceId);
        return Result.success(result);
    }

    /**
     * 增加下载次数
     * @param resourceId 资源ID
     * @return 是否成功
     */
    @PostMapping("/download/{resourceId}")
    public Result<Boolean> incrementDownloadCount(@PathVariable Long resourceId) {
        Boolean result = resourceService.incrementDownloadCount(resourceId);
        return Result.success(result);
    }
}
