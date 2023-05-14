package com.limbo.reggie.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {

    private static ThreadLocal<Long[]> threadLocal = new ThreadLocal<Long[]>(){
        @Override
        protected Long[] initialValue() {
            return new Long[2];
        }
    };

    public static void set_user_id(Long id){
        Long[] ids = threadLocal.get();
        ids[0] = id;
        threadLocal.set(ids);
    }
    public static void set_emp_id(Long id){
        Long[] ids = threadLocal.get();
        ids[1] = id;
        threadLocal.set(ids);;
    }

    public static Long get_user_id(){
        return threadLocal.get()[0];
    }
    public static Long get_emp_id(){
        return threadLocal.get()[1];
    }


}
