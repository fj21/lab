package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqu.lab.mapper.ResourceMapper;
import com.cqu.lab.model.entity.Resource;
import com.cqu.lab.model.vo.ResourceListVO;
import com.cqu.lab.model.vo.ResourceVO;
import com.cqu.lab.model.vo.UserBasicVO;
import com.cqu.lab.service.OssService;
import com.cqu.lab.service.ResourceService;
import com.cqu.lab.service.UserService;
import com.cqu.lab.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资源服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final UserService userService;
    private final OssService ossService;

    @Override
    public ResourceListVO getResourceList(Integer type, Long lastResourceId) {
        // 如果lastResourceId为0，则获取最新的资源ID
        if (lastResourceId == 0) {
            lastResourceId = resourceMapper.selectNewestResourceId();
            if (lastResourceId == null) {
                lastResourceId = 0L;
            }
        }

        // 查询资源列表
        List<Resource> resources = resourceMapper.selectResourceList(type, lastResourceId);
        
        // 构建返回结果
        ResourceListVO resourceListVO = new ResourceListVO();
        List<ResourceVO> resourceVOList = new ArrayList<>();
        
        if (resources != null && !resources.isEmpty()) {
            for (Resource resource : resources) {
                ResourceVO resourceVO = new ResourceVO();
                BeanUtils.copyProperties(resource, resourceVO);
                
                // 获取上传者信息
                try {
                    UserBasicVO userBasicVO = userService.getUserBasicInfo(resource.getUserId().intValue());
                    resourceVO.setUserBasicVO(userBasicVO);
                } catch (Exception e) {
                    log.error("获取用户信息失败", e);
                }
                
                resourceVOList.add(resourceVO);
            }
            
            // 设置最后一条资源ID
            resourceListVO.setLastResourceId(resources.get(resources.size() - 1).getId());
            resourceListVO.setHasMore(resources.size() >= 50); // 如果返回50条数据，则可能还有更多数据
        } else {
            resourceListVO.setHasMore(false);
            resourceListVO.setLastResourceId(0L);
        }
        
        resourceListVO.setResourceList(resourceVOList);
        return resourceListVO;
    }

    @Override
    public ResourceVO getResourceDetail(Long resourceId) {
        // 查询资源详情
        Resource resource = getById(resourceId);
        if (resource == null || resource.getIsDeleted() == 1) {
            return null;
        }
        
        // 构建返回结果
        ResourceVO resourceVO = new ResourceVO();
        BeanUtils.copyProperties(resource, resourceVO);
        
        // 获取上传者信息
        try {
            UserBasicVO userBasicVO = userService.getUserBasicInfo(resource.getUserId().intValue());
            resourceVO.setUserBasicVO(userBasicVO);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
        }
        
        return resourceVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean uploadResource(Integer type, String title, String description, String content, 
                                MultipartFile coverFile, MultipartFile resourceFile) {
        // 创建资源实体
        Resource resource = new Resource();
        resource.setUserId(Long.valueOf(ThreadLocalUtil.getUserId()));
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setContent(content);
        resource.setType(type);
        resource.setDownloadCount(0);
        resource.setIsDeleted(0);
        resource.setCreatedAt(new Date());
        resource.setUpdatedAt(new Date());
        
        // 上传封面图
        if (coverFile != null && !coverFile.isEmpty()) {
            try {
                String coverUrl = ossService.uploadImage(coverFile);
                resource.setCoverUrl(coverUrl);
            } catch (Exception e) {
                log.error("上传封面图失败", e);
                return false;
            }
        }
        
        // 上传资源文件
        if (resourceFile != null && !resourceFile.isEmpty()) {
            try {
                String downloadUrl = ossService.uploadFile(resourceFile, "resources");
                resource.setDownloadUrl(downloadUrl);
            } catch (Exception e) {
                log.error("上传资源文件失败", e);
                return false;
            }
        }
        
        // 保存资源
        return save(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateResource(Long resourceId, Integer type, String title, String description, String content) {
        // 查询资源
        Resource resource = getById(resourceId);
        if (resource == null || resource.getIsDeleted() == 1) {
            return false;
        }
        
        // 更新资源
        resource.setType(type);
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setContent(content);
        resource.setUpdatedAt(new Date());
        
        return updateById(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteResource(Long resourceId) {
        // 查询资源
        Resource resource = getById(resourceId);
        if (resource == null) {
            return false;
        }
        
        // 逻辑删除
        resource.setIsDeleted(1);
        resource.setUpdatedAt(new Date());
        
        return updateById(resource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean incrementDownloadCount(Long resourceId) {
        // 查询资源
        Resource resource = getById(resourceId);
        if (resource == null || resource.getIsDeleted() == 1) {
            return false;
        }
        
        // 增加下载次数
        resource.setDownloadCount(resource.getDownloadCount() + 1);
        resource.setUpdatedAt(new Date());
        
        return updateById(resource);
    }
}
