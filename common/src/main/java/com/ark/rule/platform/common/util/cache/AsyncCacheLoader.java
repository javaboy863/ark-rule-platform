
package com.ark.rule.platform.common.util.cache;

import com.google.common.cache.CacheLoader;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * 异步缓存，放入线程池.
 *
 */
public class AsyncCacheLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCacheLoader.class);

    /**
     * 功能描述: 〈异步〉.
     *
     * @param function 方法
     * @param executor 线程池
     * @return com.google.common.cache.CacheLoader
     */
    /**
     * 功能描述: 〈异步〉.
     *
     * @param function 方法
     * @param executor 线程池
     * @param <K> key泛型
     * @param <V> value泛型
     * @return com.google.common.cache.CacheLoader
     */
    public static <K, V> CacheLoader<K, V> buildAsyncCacheLoader(final Function<K, V> function,
                                                                 final Executor executor) {
        return CacheLoader.asyncReloading(new CacheLoader<K, V>() {
            @Override
            public V load(K k) throws Exception {
                LOGGER.debug("线程{}刷新缓存", Thread.currentThread().getId());
                try {
                    return function.apply(k);
                } finally {
                    LOGGER.debug("线程{}刷新缓存结束", Thread.currentThread().getId());
                }
            }
        }, executor);
    }
}

