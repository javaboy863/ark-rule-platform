package com.ark.rule.platform.common.util;

import org.springframework.beans.BeanUtils;

/**
 * 类转换工具.
 *
 */
public class BeanConvertUtil {

    /**
     * 转换对象,beanUtil属性拷贝.
     *
     * @param param 入参
     * @param clz   出参类型
     * @param <T>   泛型
     * @return ''
     */
    public static <T> T conver(Object param, Class<T> clz) {
        if (param == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(param, obj);
        return obj;
    }

}

