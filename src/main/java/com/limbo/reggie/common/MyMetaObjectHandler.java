package com.limbo.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 自定义元数据对象处理器， 处理重复代码
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.hasSetter("createTime"))
            metaObject.setValue("createTime", LocalDateTime.now());
        if(metaObject.hasSetter("updateTime"))
            metaObject.setValue("updateTime", LocalDateTime.now());
        if(metaObject.hasSetter("createUser"))
            metaObject.setValue("createUser", BaseContext.get_emp_id());
        if(metaObject.hasSetter("updateUser"))
            metaObject.setValue("updateUser", BaseContext.get_emp_id());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.get_emp_id());
    }
}
