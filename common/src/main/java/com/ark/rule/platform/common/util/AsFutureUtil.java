package com.ark.rule.platform.common.util;

import com.ark.rule.platform.common.domain.AsFuture;

import java.util.concurrent.*;

/**
 * 异步执行工具类.
 *
 */
public class AsFutureUtil {

    private static volatile ExecutorService executor = null;

    /**
     * 默认最小线程数目
     */
    private static final Integer MIN_THREAD_POOL_SIZE = 200;

    /**
     * 默认最大线程数目
     */
    private static final Integer MAX_THREAD_POOL_SIZE = 1000;

    /**
     * 默认线程的生存时间, 单位是秒
     */
    private static final Integer THREAD_KEEP_ALIVE_TIME_IN_SECOND = 30;

    /**
     * @param minPoolSize   ''
     * @param maxPoolSize   ''
     * @param keepAliveTime ''
     * @desc spring构造函数
     */
    public AsFutureUtil(Integer minPoolSize, Integer maxPoolSize, Integer keepAliveTime) {
        init(minPoolSize, maxPoolSize, keepAliveTime);
    }

    /**
     * @param minPoolSize ''
     * @param maxPoolSize ''
     * @desc spring构造函数
     */
    public AsFutureUtil(Integer minPoolSize, Integer maxPoolSize) {
        init(minPoolSize, maxPoolSize, THREAD_KEEP_ALIVE_TIME_IN_SECOND);
    }

    /**
     * @desc 如果在spring中没有配置线程池的大小，执行默认的线程池初始化
     */
    private static void defaultInit() {
        init(MIN_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE, THREAD_KEEP_ALIVE_TIME_IN_SECOND);
    }

    /**
     * 初始化并发线程池，类似CachedThreadPool.
     *
     * @param minPoolSize   线程池的最小线程数, 根据业务设定
     * @param maxPollSize   线程池的最大线程数，根据业务设定
     * @param keepAliveTime 线程在没有执行任务后的存活时间， 单位是秒 , 一般设置为60即可
     */
    public static synchronized void init(Integer minPoolSize, Integer maxPollSize, Integer keepAliveTime) {
        if (executor == null) {
            executor =
                    new ThreadPoolExecutor(
                            minPoolSize,
                            maxPollSize,
                            keepAliveTime,
                            TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>());

        }
    }

    /**
     * @param runnable ''
     * @return ''
     * @desc 添加一个任务, 对于runnable返回这个对象是为了上层能够接受底层的异常
     */
    public static AsFuture run(Runnable runnable) {
        if (executor == null) {
            defaultInit();
        }
        String logUrl = ThreadLocalContainer.getURL();
        AsFuture ret = new AsFuture<>();
        ret.setFuture(executor.submit(new Runnable() {
            @Override
            public void run() {
                ThreadLocalContainer.setURI(logUrl);
                runnable.run();
            }
        }));
        return ret;
    }

    /**
     * @param callable ''
     * @param <T>      ''
     * @return ''
     * @desc 添加一个待返回值的任务
     */
    public static <T> AsFuture<T> run(Callable<T> callable) {
        if (executor == null) {
            defaultInit();
        }
        AsFuture<T> ret = new AsFuture<>();
        String logUrl = ThreadLocalContainer.getURL();
        ret.setFuture(executor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                ThreadLocalContainer.setURI(logUrl);
                return callable.call();
            }
        }));
        return ret;
    }

    /**
     * 功能描述: 〈取出ExecutorService〉.
     *
     * @return java.util.concurrent.ExecutorService
     */
    public static ExecutorService getExecutor() {
        if (executor == null) {
            defaultInit();
        }
        return executor;
    }
}

