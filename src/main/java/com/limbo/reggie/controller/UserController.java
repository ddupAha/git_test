package com.limbo.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.node.POJONode;
import com.limbo.reggie.common.Result;
import com.limbo.reggie.entity.User;
import com.limbo.reggie.service.UserService;
import com.limbo.reggie.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author limbo
 * @since 2023-05-11
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
        if(StringUtils.isNotEmpty(user.getPhone())){
            // 生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码：" + code);

            // 调用阿里云API发送短信

//            session.setAttribute(user.getPhone(), code);

            redisTemplate.opsForValue().set(user.getPhone(), code, 5, TimeUnit.MINUTES); // 使用redis缓存验证码

            return Result.success("发送验证码成功");

        }
        return Result.error("短信发送失败");
    }


    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> map, HttpSession session){
        // 获取手机号和验证码
        String phone = map.get("phone");
        String code = map.get("code");

        // 判断验证码是否正确
        String code_real = null;
        if (code != null) {
            code_real = (String) redisTemplate.opsForValue().get(phone); // redis中取验证码
        }
        else
            return Result.error("验证码为空");


        if(phone == null)
            return Result.error("手机号不正确");

        // TODO: 实际上还要再判断发送验证码的手机号和当前填入的手机号是否一样


        // 手机号和验证码 都不为空
        if(code.equals(code_real)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);

            //第一次登录，要创建用户
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            redisTemplate.delete(phone); // 登录成功则删除缓存验证码
            return Result.success("登录成功");
        }
        return Result.error("登录失败");
    }


    @PostMapping("/loginout")
    public Result<String> loginout(HttpSession session){
        session.removeAttribute("user");
        return Result.success("退出成功");
    }



}
