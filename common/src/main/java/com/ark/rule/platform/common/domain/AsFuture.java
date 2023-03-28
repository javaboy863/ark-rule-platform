
package com.ark.rule.platform.common.domain;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 封装future.
 *
 * @param <T> 泛型
 */
public class AsFuture<T> {
    private Future<T> future;

    /**
     * 封装是为了是实现底层异常的业务透明.
     *
     * @return ''
     * @throws Exception ''
     */
    public T getData() throws Exception {
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw (Exception) e.getCause();
        }
    }

    public void setFuture(Future<T> future) {
        this.future = future;
    }
}

