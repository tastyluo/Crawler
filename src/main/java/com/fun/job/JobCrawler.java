package com.fun.job;

import com.fun.model.JDGoodsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-02 22:51
 */
@Component
public class JobCrawler {

    @Qualifier("JDGoodsMapperPipeline")
    @Autowired
    private PageModelPipeline jdgoodsMapperPipeline;

    public void crawl() {
        OOSpider.create(Site.me().setSleepTime(1000).setRetryTimes(3)
                , jdgoodsMapperPipeline, JDGoodsInfo.class)
                .addUrl("https://list.jd.com/list.html?cat=670,677,678&page=0").thread(5).run();
    }

}
