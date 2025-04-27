package com.cqu.lab.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqu.lab.model.entity.Resource;
import com.cqu.lab.model.vo.ResourceListVO;
import com.cqu.lab.model.vo.ResourceVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源服务接口
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 获取资源列表
     * @param type 资源类型
     * @param lastResourceId 上一页最后一条资源ID
     * @return 资源列表
     */
    ResourceListVO getResourceList(Integer type, Long lastResourceId);

    /**
     * 获取资源详情
     * @param resourceId 资源ID
     * @return 资源详情
     */
    ResourceVO getResourceDetail(Long resourceId);

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
    Boolean uploadResource(Integer type, String title, String description, String content, 
                          MultipartFile coverFile, MultipartFile resourceFile);

    /**
     * 更新资源
     * @param resourceId 资源ID
     * @param type 资源类型
     * @param title 标题
     * @param description 描述
     * @param content 内容
     * @return 是否成功
     */
    Boolean updateResource(Long resourceId, Integer type, String title, String description, String content);

    /**
     * 删除资源
     * @param resourceId 资源ID
     * @return 是否成功
     */
    Boolean deleteResource(Long resourceId);

    /**
     * 增加下载次数
     * @param resourceId 资源ID
     * @return 是否成功
     */
    Boolean incrementDownloadCount(Long resourceId);
}
