package com.limbo.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.dto.SetmealDto;
import com.limbo.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐 服务类
 * </p>
 *
 * @author limbo
 * @since 2023-05-08
 */
public interface SetmealService extends IService<Setmeal> {

    Page page(int page, int pageSize, String name);

    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
