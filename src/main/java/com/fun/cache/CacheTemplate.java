package com.fun.cache;

import java.util.Map;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-06 10:24
 * 缓存模版
 */
interface CacheTemplate<T> {
    T get(String key);
    Map<String, T> getAll();
    void put(T obj);
    void clear();
}
