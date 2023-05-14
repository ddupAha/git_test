package com.limbo.reggie.common;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，捕获异常
 */

@ControllerAdvice(annotations = {RestController.class}) // 表示针对有RestController的类
@ResponseBody
public class GlobalExceptionHandler {

    //表示处理SQLIntegrityConstraintViolationException这个异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> handler(SQLIntegrityConstraintViolationException exception){
        System.out.println(exception);
        return Result.error("插入员工失败");
    }

    @ExceptionHandler(CustomException.class)
    public Result<String> customHandler(CustomException exception){
        return Result.error(exception.getMessage());
    }


}
