package com.limbo.reggie.service;

import com.limbo.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务类
 * </p>
 *
 * @author limbo
 * @since 2023-05-07
 */
public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
