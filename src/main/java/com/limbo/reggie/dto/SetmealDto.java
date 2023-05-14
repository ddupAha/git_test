package com.limbo.reggie.dto;


import com.limbo.reggie.entity.Setmeal;
import com.limbo.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
