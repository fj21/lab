package com.cqu.lab.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 资源列表VO
 */
@Data
public class ResourceListVO implements Serializable {
    /**
     * 资源列表
     */
    private List<ResourceVO> resourceList;

    /**
     * 是否有更多数据
     */
    private Boolean hasMore;

    /**
     * 最后一条资源ID
     */
    private Long lastResourceId;

    private static final long serialVersionUID = 1L;
}
