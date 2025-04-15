package com.cqu.lab.mapper;

import com.cqu.lab.model.entity.PostCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jinhong Jiang
* @description 针对表【post_collect(用户收藏表)】的数据库操作Mapper
* @createDate 2025-04-13 14:43:06
* @Entity com.cqu.lab.model.entity.PostCollect
*/
@Mapper
public interface PostCollectMapper extends BaseMapper<PostCollect> {

}




