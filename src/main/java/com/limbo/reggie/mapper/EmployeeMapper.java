package com.limbo.reggie.mapper;

import com.limbo.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author limbo
 * @since 2023-05-06
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
