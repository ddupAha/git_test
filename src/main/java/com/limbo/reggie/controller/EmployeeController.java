package com.limbo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.entity.Employee;
import com.limbo.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * <p>
 * 员工信息 后台登录控制器
 * </p>
 *
 * @author limbo
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;


    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 判断是否存在该用户名（数据库中设置的username字段为唯一）
        if(emp == null){
            return Result.error("登陆失败");
        }
        // 密码加密比较数据库
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        if(!password.equals(emp.getPassword())){
            return Result.error("登陆失败");
        }
        if(emp.getStatus() == 0){
            return Result.error("用户被禁用");
        }
        request.getSession().setAttribute("emp", emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("emp");
        return Result.success("退出成功");
    }

    /**
     * 添加员工，初始密码为123456
     * @param request
     * @param employee
     * @return
     */

    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) request.getSession().getAttribute("emp"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("emp"));

        employeeService.save(employee);
        return Result.success("新增员工成功");
    }

    /**
     * 员工信息分页显示，查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        Page<Employee> pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getUsername, name)
                .orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, queryWrapper);

        return Result.success(pageInfo);

    }

    /**
     * 根据id修改员工（包含禁用状态和修改员工信息）
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> updateEmployee(HttpServletRequest request, @RequestBody Employee employee){
//        employee.setUpdateUser((Long) request.getSession().getAttribute("emp"));
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);
        return Result.success("修改用户成功");
    }

    @GetMapping("/{id}")
    public Result<Employee> queryById(@PathVariable Long id){

        Employee emp = employeeService.getById(id);
        if(emp != null)
            return Result.success(emp);
        return Result.error("没有找到该用户");
    }


}

