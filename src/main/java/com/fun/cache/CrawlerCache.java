package com.fun.cache;

import com.fun.entity.JdGoods;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-06 10:23
 */
@Component
public class CrawlerCache implements CacheTemplate<JdGoods> {

    private static final Map<String, JdGoods> cache = new ConcurrentHashMap<>();

    @Override
    public JdGoods get(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return null;
    }

    @Override
    public Map<String, JdGoods> getAll() {
        Map<String, JdGoods> jdGoodsMap = new HashMap<>();
        synchronized (cache) {
            jdGoodsMap.putAll(cache);
            clear();
        }
        return jdGoodsMap;
    }

    @Override
    public void put(JdGoods jdGoods) {
        if (jdGoods != null) {
            cache.put(jdGoods.getGoodsId(), jdGoods);
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
