package com.limbo.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.dto.SetmealDto;
import com.limbo.reggie.entity.Setmeal;
import com.limbo.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 套餐 前端控制器
 * </p>
 *
 * @author limbo
 * @since 2023-05-08
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @GetMapping("/page")
    public Result<Page> getPage(int page, int pageSize, String name){
        return Result.success(setmealService.page(page, pageSize, name));
    }



    @PostMapping
    public Result<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return Result.success("新增菜品成功");
    }


    @DeleteMapping
    public Result<String> removeWithDish(@RequestParam List<Long> ids){
//        System.out.println(ids.toString());
        setmealService.removeWithDish(ids);
        return Result.success("删除套餐成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return Result.success(list);
    }
}
