package com.limbo.reggie.dto;

import com.limbo.reggie.entity.Dish;
import com.limbo.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    // 菜品口味
    private List<DishFlavor> flavors = new ArrayList<>();

    // 展示的菜品分类名称
    private String categoryName;

    private Integer copies;
}
