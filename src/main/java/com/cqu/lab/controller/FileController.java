package com.cqu.lab.controller;


import com.cqu.lab.model.common.Result;
import com.cqu.lab.service.OssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for file uploads
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final OssService ossService;

    /**
     * Upload image file
     * @param file Image file
     * @return URL of uploaded image
     */
    @PostMapping("/upload/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.failed("File is empty");
        }

        try {
            String fileUrl = ossService.uploadImage(file);
            log.info("Uploaded image: {}", fileUrl);
            
            Map<String, String> data = new HashMap<>();
            data.put("url", fileUrl);
            return Result.success(data);
        } catch (Exception e) {
            log.error("Failed to upload image", e);
            return Result.failed("Failed to upload image: " + e.getMessage());
        }
    }

    /**
     * Upload video file
     * @param file Video file
     * @param cover Optional cover image
     * @return URLs of uploaded files
     */
    @PostMapping("/upload/video")
    public Result<Map<String, String>> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "cover", required = false) MultipartFile cover) {
        
        if (file.isEmpty()) {
            return Result.failed("Video file is empty");
        }

        try {
            String videoUrl = ossService.uploadVideo(file);
            log.info("Uploaded video: {}", videoUrl);
            
            String coverUrl = null;
            if (cover != null && !cover.isEmpty()) {
                coverUrl = ossService.uploadCover(cover);
                log.info("Uploaded video cover: {}", coverUrl);
            }
            
            Map<String, String> data = new HashMap<>();
            data.put("videoUrl", videoUrl);
            if (coverUrl != null) {
                data.put("coverUrl", coverUrl);
            }
            
            return Result.success(data);
        } catch (Exception e) {
            log.error("Failed to upload video", e);
            return Result.failed("Failed to upload video: " + e.getMessage());
        }
    }

    /**
     * Delete file from OSS
     * @param fileUrl URL of file to delete
     * @return Success or failure
     */
    @DeleteMapping("/delete")
    public Result<Boolean> deleteFile(@RequestParam("url") String fileUrl) {
        try {
            boolean deleted = ossService.deleteFile(fileUrl);
            if (deleted) {
                log.info("Deleted file: {}", fileUrl);
                return Result.success(true);
            } else {
                log.warn("File not found: {}", fileUrl);
                return Result.failed("File not found");
            }
        } catch (Exception e) {
            log.error("Failed to delete file", e);
            return Result.failed("Failed to delete file: " + e.getMessage());
        }
    }
}
