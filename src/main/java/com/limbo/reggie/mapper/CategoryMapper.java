package com.limbo.reggie.mapper;

import com.limbo.reggie.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜品及套餐分类 Mapper 接口
 * </p>
 *
 * @author limbo
 * @since 2023-05-07
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
