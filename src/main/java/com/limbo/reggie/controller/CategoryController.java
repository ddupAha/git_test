package com.limbo.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.entity.Category;
import com.limbo.reggie.entity.Dish;
import com.limbo.reggie.service.CategoryService;
import com.limbo.reggie.service.DishService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜品及套餐分类 前端控制器
 * </p>
 *
 * @author limbo
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 分页显示 分类管理界面
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> getCategoryList(int page, int pageSize){
        Page<Category> pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo);

    }

    /**
     * 新增分类
     * @param request
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> addCategory(HttpServletRequest request, @RequestBody Category category){
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser((Long) request.getSession().getAttribute("emp"));
        category.setUpdateUser((Long) request.getSession().getAttribute("emp"));

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Category::getName, category.getName());
        if(categoryService.getOne(queryWrapper) != null){
            return Result.error("添加失败，已经有该菜品分类");
        }

        categoryService.save(category);
        return Result.success("添加菜品分类成功");
    }

    /**
     * 通过id删除分类（需要判断该分类下有无具体菜品，有的话不能删除）
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteById(Long ids){

        categoryService.remove(ids); // 自己写的remove方法
        return Result.success("删除成功");
    }

    /**
     * 通过id修改分类
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> updateById(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("修改分类成功");
    }


    /**
     * 根据type查询有哪些菜品分类
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> getDishCategoryList(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null, Category::getType, category.getType())
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }

}
