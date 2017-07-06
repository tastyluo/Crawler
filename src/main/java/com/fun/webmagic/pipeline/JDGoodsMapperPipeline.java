package com.fun.webmagic.pipeline;

import com.fun.cache.CrawlerCache;
import com.fun.entity.JdGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-30 10:46
 * 缓存商品信息
 */
@Component("JDGoodsMapperPipeline")
public class JDGoodsMapperPipeline implements Pipeline {

    @Autowired
    private CrawlerCache crawlerCache;

    @Override
    public void process(ResultItems resultItems, Task task) {
        JdGoods jdGoods = resultItems.get("jdGoods");
        crawlerCache.put(jdGoods);
    }
}
