package com.limbo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.dto.DishDto;
import com.limbo.reggie.entity.Category;
import com.limbo.reggie.entity.Dish;
import com.limbo.reggie.entity.DishFlavor;
import com.limbo.reggie.mapper.DishMapper;
import com.limbo.reggie.service.CategoryService;
import com.limbo.reggie.service.DishFlavorService;
import com.limbo.reggie.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜品管理 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2023-05-07
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;

    /**
     * 新增菜品，需要同时对dish以及dishFlavor表进行操作。因为新增菜品还包含了口味。
     * @param dishDto
     */
    @Transactional  // 加入事务保证表的一致性
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).toList();  // 使用stream进行操作，也可以用for

        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 分页展示菜品，需要查询菜品的分类名称
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Result<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        // 因为需要使用queryWrapper来进行name的查询，但是DTO没有对应的表进行。所以需要使用原始Dish，来进行扩展到DishDTO
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name)
                .orderByDesc(Dish::getUpdateTime);
        this.page(pageInfo, queryWrapper);  // 把查询数据放入pageInfo

        Page<DishDto> dishDtoPage = new Page<>(); //要返回展示的结果，包含了分类名称
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            dishDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());

            return dishDto;

        }).toList();
        dishDtoPage.setRecords(dishDtoList);

        return Result.success(dishDtoPage);
    }

    /**
     * 通过id获取Dish，封装成DTO传给前端
     * @param id
     * @return
     */
    @Override
    public DishDto getDishById(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

        Category category = categoryService.getById(dish.getCategoryId());

        dishDto.setFlavors(list);
        dishDto.setCategoryName(category.getName());
        return dishDto;
    }


    /**
     * 更新dish，对应同时需要更新dishFlavor
     * @param dishDto
     */
    @Override
    public void updateDishWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaUpdateWrapper<DishFlavor> updateWrapper = new LambdaUpdateWrapper<>();

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).toList();

        dishFlavorService.saveBatch(flavors);
    }
}
