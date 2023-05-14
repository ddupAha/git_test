package com.limbo.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.limbo.reggie.common.BaseContext;
import com.limbo.reggie.common.Result;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;


@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    private static final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();

        //允许访问的uri
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",  // **需要使用AntPathMatcher进行匹配
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };
        //判断uri是否允许
        boolean check = check(uris, uri);
//        System.out.println(uri  + " 允许通过？：" + check);
        //允许通过
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }

        // 判断后台管理员登录
        if (request.getSession().getAttribute("emp") != null) {
            BaseContext.set_emp_id((Long) request.getSession().getAttribute("emp"));
            filterChain.doFilter(request, response);
            return;
        }

        // TODO: 这里有一个问题：前台用户登录了，后台也可以直接进入，这是错误的

        // 判断前台客户登录
        if (request.getSession().getAttribute("user") != null) {
            BaseContext.set_user_id((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
            return;
        }

        //不属于上面的情况，要进行拦截
        // 在静态资源 request.js中, 也就是前端页面里已经写了对应的拦截器，只要返回的msg为NOTLOGIN，自动进行跳转
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));

    }

    private boolean check(String[] uris, String uri) {
        for (String s : uris) {
            if (matcher.match(s, uri)) {
                return true;
            }
        }
        return false;
    }
}
