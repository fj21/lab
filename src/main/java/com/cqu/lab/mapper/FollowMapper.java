package com.cqu.lab.mapper;

import com.cqu.lab.model.entity.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jinhong Jiang
* @description 针对表【follow(用户关注表)】的数据库操作Mapper
* @createDate 2025-04-13 14:43:09
* @Entity com.cqu.lab.model.entity.Follow
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}




