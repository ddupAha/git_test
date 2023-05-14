package com.limbo.reggie.service.impl;

import com.limbo.reggie.entity.Employee;
import com.limbo.reggie.mapper.EmployeeMapper;
import com.limbo.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2023-05-06
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
