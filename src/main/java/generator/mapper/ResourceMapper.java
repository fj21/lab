package generator.mapper;

import generator.domain.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Jinhong Jiang
* @description 针对表【resource(资源表)】的数据库操作Mapper
* @createDate 2025-04-28 23:45:46
* @Entity generator.domain.Resource
*/
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 查询资源列表
     * @param type 资源类型
     * @param lastResourceId 上一页最后一条资源ID
     * @return 资源列表
     */
    @Select("<script>" +
            "SELECT * FROM resource WHERE is_deleted = 0 " +
            "<if test='type != null'> AND type = #{type} </if>" +
            "<if test='lastResourceId != null and lastResourceId != 0'> AND id &lt; #{lastResourceId} </if>" +
            "ORDER BY created_at DESC LIMIT 50" +
            "</script>")
    List<com.cqu.lab.model.entity.Resource> selectResourceList(Integer type, Long lastResourceId);

    /**
     * 查询最新资源ID
     * @return 最新资源ID
     */
    @Select("SELECT id FROM resource WHERE is_deleted = 0 ORDER BY created_at DESC LIMIT 1")
    Long selectNewestResourceId();
}




