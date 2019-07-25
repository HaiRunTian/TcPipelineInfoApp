package com.app.pipelinesurvey.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author linshen on 2019-06-29.
 * 邮箱: 18475453284@163.com
 */
public class ThreadUtils {
    /**
     *   池中所保存的线程数，包括空闲线程。
     */
    private static int corePoolSize = 5;
    /**
     *  池中允许的最大线程数。
     */
    private static int maximumPoolSize = 10;

    /**
     * 当线程数大于核心线程时，此为终止前多余的空闲线程等待新任务的最长时间
     */
    private static long keepAliveTime = 200;

    private static ThreadPoolExecutor executor;

    /**
     * 执行前用于保持任务的队列，即任务缓存队列
     */
    private static ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);
    /**
     *
     * @param task
     */
    public static void execute(Runnable task) {
        if (executor == null) {
            executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue);
        }
        executor.submit(task);
    }
}
