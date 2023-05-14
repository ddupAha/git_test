package com.limbo.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.dto.DishDto;
import com.limbo.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * 菜品管理 服务类
 * </p>
 *
 * @author limbo
 * @since 2023-05-07
 */
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public Result<Page> page(int page, int pageSize, String name);

    public DishDto getDishById(Long id);

    public void updateDishWithFlavor(DishDto dishDto);

}
