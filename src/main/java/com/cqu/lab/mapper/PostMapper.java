package com.cqu.lab.mapper;

import com.cqu.lab.model.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Jinhong Jiang
* @description 针对表【post(帖子表)】的数据库操作Mapper
* @createDate 2025-04-13 09:56:12
* @Entity com.cqu.lab.model.entity.Post
*/
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    @Select("SELECT * FROM post WHERE category = #{category} AND id < #{lastPostId} " +
            "ORDER BY created_at DESC LIMIT 50")
    List<Post> selectSectionPost(Integer category, Integer lastPostId);

    @Select("SELECT id FROM post ORDER BY created_at DESC LIMIT 1")
    Integer selectNewestPost();
}




