package com.limbo.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.dto.DishDto;
import com.limbo.reggie.entity.Category;
import com.limbo.reggie.entity.Dish;
import com.limbo.reggie.entity.DishFlavor;
import com.limbo.reggie.service.CategoryService;
import com.limbo.reggie.service.DishFlavorService;
import com.limbo.reggie.service.DishService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜品管理 前端控制器
 * </p>
 *
 * @author limbo
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public Result<Page> getPage(int page, int pageSize, String name){
        return dishService.page(page, pageSize, name);
    }


    /**
     * 添加菜品。自定义service方法
     * @param dishDto
     * @return
     */

    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return Result.success("添加菜品成功");

    }

    /**
     * 通过id获取Dish
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getDishById(@PathVariable Long id){
        return Result.success(dishService.getDishById(id));
    }

    @PutMapping
    public Result<String> updateDish(@RequestBody DishDto dishDto){
        dishService.updateDishWithFlavor(dishDto);
        return Result.success("修改菜品成功");
    }

    /**
     * 通过分类ID获取菜品列表
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> queryDishListByCategoryId(@RequestParam Long categoryId){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null ,Dish::getCategoryId,categoryId);
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long item_id = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(item_id);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).toList();



        return Result.success(dishDtoList);
    }



}
