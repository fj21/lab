package com.cqu.lab.mapper;

import com.cqu.lab.model.entity.PostLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jinhong Jiang
* @description 针对表【post_like(用户点赞表)】的数据库操作Mapper
* @createDate 2025-04-13 14:43:02
* @Entity com.cqu.lab.model.entity.PostLike
*/
@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {

}




