package com.cqu.lab.utils;

/**
 * @author jiang jinhong
 * @date 2025/1/29 9:11
 * @description threadLocal工具类
 */
public class ThreadLocalUtil {
    private final static ThreadLocal<Integer> THREAD_LOCAL_USER_ID = new ThreadLocal<>();

    /**
     * 获取当前线程的 userId
     * @return
     */
    public static Integer getUserId(){
        return THREAD_LOCAL_USER_ID.get();
    }

    /**
     * 设置当前线程的 userId
     * @param userId userId
     */
    public static void setUserId(Integer userId){
        THREAD_LOCAL_USER_ID.set(userId);
    }

    /**
     * 移除当前线程对应的 userId，避免线程回归线程池被其他任务使用
     */
    public static void clear(){
        THREAD_LOCAL_USER_ID.remove();
    }


}
