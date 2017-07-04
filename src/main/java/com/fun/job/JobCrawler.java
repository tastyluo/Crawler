package com.fun.job;

import com.fun.pipeline.JDGoodsMapperPipeline;
import com.fun.processor.JDGoodsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-02 22:51
 */
@Component
public class JobCrawler {

    @Autowired
    private JDGoodsMapperPipeline jdGoodsMapperPipeline;

    @Scheduled(cron = "0 0 0/1 * * ?") //每小时执行一次
    public void crawl() {
        Spider.create(new JDGoodsProcessor())
                .addPipeline(jdGoodsMapperPipeline)
                .addUrl("https://list.jd.com/list.html?cat=670,677,678&page=0")
                .thread(5)
                .run();
    }

}
