package com.limbo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.limbo.reggie.common.CustomException;
import com.limbo.reggie.entity.Category;
import com.limbo.reggie.entity.Dish;
import com.limbo.reggie.entity.Setmeal;
import com.limbo.reggie.mapper.CategoryMapper;
import com.limbo.reggie.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limbo.reggie.service.DishService;

import com.limbo.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2023-05-07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    /**
     * 通过ID删除分类，需要判断分类下是否有菜品，有的话不能删除
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        if(dishService.count(dishQueryWrapper) > 0){
            //抛出自定义异常
            throw new CustomException("该分类下有菜品，不能删除");
        }


        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if(setmealService.count(setmealLambdaQueryWrapper) > 0){
            throw new CustomException("该分类下有套餐，不能删除");
        }

        super.removeById(id);

    }
}
