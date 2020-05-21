package com.chengql.template.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * 多任务工具类
 */
public class MultitaskRunnableUtil {
    /**
     * 线程对列
     */
    private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
            20); // 固定为20的线程队列
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 10,
            1, TimeUnit.HOURS, queue);
    
    
    /**
    *
    * 实现 Runnable 接口类
    */
    public static  void run(Runnable runnable) {
        executor.execute(runnable);
    }
}
