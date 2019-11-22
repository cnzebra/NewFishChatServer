package org.miaohong.newfishchatserver.core.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
    /**
     * 普通任务优先级，默认0
     */
    public static int THREAD_PRIORITY_NORMAL = 0;

    /**
     * 高任务优先级，默认10
     */
    public static int THREAD_PRIORITY_HIGH = 10;
    /**
     * 低任务优先级，默认-10
     */
    public static int THREAD_PRIORITY_LOW = -10;

    /**
     * 固定大小线程池，无队列
     *
     * @param corePoolSize 初始化线程池
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize) {
        return new ThreadPoolExecutor(corePoolSize,
                corePoolSize,
                0,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>());
    }

    /**
     * 固定大小线程池，自定义队列
     *
     * @param corePoolSize 初始化线程池
     * @param queue        线程池队列
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize,
                                                        BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(corePoolSize,
                corePoolSize,
                0,
                TimeUnit.MILLISECONDS,
                queue);
    }

    /**
     * 固定大小线程池，自定义队列和线程池工厂
     *
     * @param corePoolSize  初始化线程池
     * @param queue         线程池队列
     * @param threadFactory 线程池工厂
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize,
                                                        BlockingQueue<Runnable> queue,
                                                        ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize,
                corePoolSize,
                0,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory);
    }

    /**
     * 固定大小线程池，自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize  初始化线程池
     * @param queue         线程池队列
     * @param threadFactory 线程池工厂
     * @param handler       拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize,
                                                        BlockingQueue<Runnable> queue,
                                                        ThreadFactory threadFactory,
                                                        RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize,
                corePoolSize,
                0,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），无队列
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize,
                                                         int maximumPoolSize) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                DateUtils.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>());
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queue           线程池队列
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize,
                                                         int maximumPoolSize,
                                                         BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                DateUtils.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                queue);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列和线程池工厂
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queue           线程池队列
     * @param threadFactory   线程池工厂
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize,
                                                         int maximumPoolSize,
                                                         BlockingQueue<Runnable> queue,
                                                         ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                DateUtils.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param queue           线程池队列
     * @param threadFactory   线程池工厂
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize,
                                                         int maximumPoolSize,
                                                         BlockingQueue<Runnable> queue,
                                                         ThreadFactory threadFactory,
                                                         RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                DateUtils.MILLISECONDS_PER_MINUTE,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 缓冲线程池（1分钟无调用销毁），自定义队列、线程池工厂和拒绝策略
     *
     * @param corePoolSize    初始化线程池
     * @param maximumPoolSize 最大线程池
     * @param keepAliveTime   回收时间
     * @param queue           线程池队列
     * @param threadFactory   线程池工厂
     * @param handler         拒绝策略
     * @return the thread pool executor
     */
    public static ThreadPoolExecutor newCachedThreadPool(int corePoolSize,
                                                         int maximumPoolSize,
                                                         int keepAliveTime,
                                                         BlockingQueue<Runnable> queue,
                                                         ThreadFactory threadFactory,
                                                         RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 构建队列
     *
     * @param size 队列大小
     * @return 队列
     */
    public static BlockingQueue<Runnable> buildQueue(int size) {
        return buildQueue(size, false);
    }

    /**
     * 构建队列
     *
     * @param size       队列大小
     * @param isPriority 是否优先级队列
     * @return 队列
     */
    public static BlockingQueue<Runnable> buildQueue(int size, boolean isPriority) {
        BlockingQueue<Runnable> queue;
        if (size == 0) { // 默认无队列
            queue = new SynchronousQueue<>();
        } else { // 有限队列或无限队列
            if (isPriority) {
                queue = size < 0 ? new PriorityBlockingQueue<>()
                        : new PriorityBlockingQueue<>(size);
            } else {
                queue = size < 0 ? new LinkedBlockingQueue<>()
                        : new LinkedBlockingQueue<>(size);
            }
        }
        return queue;
    }
}