package com.fun.schedule;

import com.fun.webmagic.pipeline.JDGoodsMapperPipeline;
import com.fun.webmagic.processor.JDGoodsProcessor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-02 22:51
 */
@Service
public class JobCrawler implements Job {

    @Autowired
    JDGoodsMapperPipeline jdGoodsMapperPipeline;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        crawl();
    }

    public void crawl() {
        Spider.create(new JDGoodsProcessor())
                .addPipeline(jdGoodsMapperPipeline)
                .addUrl("https://list.jd.com/list.html?cat=670,677,678&page=0")
                .thread(5)
                .run();
    }
}
