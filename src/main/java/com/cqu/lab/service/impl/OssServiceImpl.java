package com.cqu.lab.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.cqu.lab.config.OssProperties;
import com.cqu.lab.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Implementation of OSS service for Aliyun
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String uploadFile(MultipartFile file, String directory) {
        // Get OSS client
        OSS ossClient = new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret());

        try {
            // Get file input stream
            InputStream inputStream = file.getInputStream();
            
            // Get original filename
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "file_" + System.currentTimeMillis();
            }
            
            // Generate unique filename
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // Create date-based directory structure
            String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            
            // Final file path in OSS
            String filePath = directory + "/" + datePath + "/" + uuid + fileExtension;
            
            // Set metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(getContentType(fileExtension));
            
            // Upload file to OSS
            ossClient.putObject(
                    new PutObjectRequest(
                            ossProperties.getBucketName(), 
                            filePath, 
                            inputStream,
                            metadata));
            
            // Return the URL of the uploaded file
            return ossProperties.getUrlPrefix() + filePath;
        } catch (IOException e) {
            log.error("Failed to upload file to OSS", e);
            throw new RuntimeException("Failed to upload file to OSS", e);
        } finally {
            // Close OSS client
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    @Override
    public String uploadImage(MultipartFile file) {
        return uploadFile(file, "post/images");
    }
    
    @Override
    public String uploadVideo(MultipartFile file) {
        return uploadFile(file, "post/videos");
    }
    
    @Override
    public String uploadCover(MultipartFile file) {
        return uploadFile(file, "post/covers");
    }
    
    @Override
    public boolean deleteFile(String fileUrl) {
        // Extract file path from URL
        String filePath = fileUrl.replace(ossProperties.getUrlPrefix(), "");
        
        // Get OSS client
        OSS ossClient = new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret());
        
        try {
            // Check if file exists
            boolean exists = ossClient.doesObjectExist(ossProperties.getBucketName(), filePath);
            if (!exists) {
                log.warn("File does not exist in OSS: {}", filePath);
                return false;
            }
            
            // Delete file from OSS
            ossClient.deleteObject(ossProperties.getBucketName(), filePath);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file from OSS", e);
            throw new RuntimeException("Failed to delete file from OSS", e);
        } finally {
            // Close OSS client
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    /**
     * Helper method to determine content type based on file extension
     * @param fileExtension File extension including the dot
     * @return Content type string
     */
    private String getContentType(String fileExtension) {
        fileExtension = fileExtension.toLowerCase();
        if (fileExtension.equals(".jpg") || fileExtension.equals(".jpeg")) {
            return "image/jpeg";
        } else if (fileExtension.equals(".png")) {
            return "image/png";
        } else if (fileExtension.equals(".gif")) {
            return "image/gif";
        } else if (fileExtension.equals(".mp4")) {
            return "video/mp4";
        } else if (fileExtension.equals(".mov")) {
            return "video/quicktime";
        } else if (fileExtension.equals(".pdf")) {
            return "application/pdf";
        } else if (fileExtension.equals(".doc") || fileExtension.equals(".docx")) {
            return "application/msword";
        } else if (fileExtension.equals(".xls") || fileExtension.equals(".xlsx")) {
            return "application/vnd.ms-excel";
        } else {
            return "application/octet-stream";
        }
    }
}
