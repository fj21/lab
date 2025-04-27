package com.cqu.lab.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service for Aliyun OSS operations
 */
public interface OssService {
    /**
     * Upload file to specified directory
     * @param file File to upload
     * @param directory Directory in OSS
     * @return URL of uploaded file
     */
    String uploadFile(MultipartFile file, String directory);
    
    /**
     * Upload image file
     * @param file Image file
     * @return URL of uploaded image
     */
    String uploadImage(MultipartFile file);
    
    /**
     * Upload video file
     * @param file Video file
     * @return URL of uploaded video
     */
    String uploadVideo(MultipartFile file);
    
    /**
     * Upload cover image for video
     * @param file Cover image
     * @return URL of uploaded cover
     */
    String uploadCover(MultipartFile file);
    
    /**
     * Delete file from OSS
     * @param fileUrl URL of file to delete
     * @return true if successful
     */
    boolean deleteFile(String fileUrl);
}
