package com.fun.schedule;

import com.fun.webmagic.pipeline.JDGoodsMapperPipeline;
import com.fun.webmagic.processor.JDProductProcessor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-02 22:51
 */
@Service
public class JobCrawler implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCrawler.class);

    @Autowired
    JDGoodsMapperPipeline jdGoodsMapperPipeline;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        crawl();
    }

    @Value("${images.path}")
    private String imgPath;

    public void crawl() {
        LOGGER.info("[******* 京东爬虫启动 *******]");
        try {
            Spider.create(new JDProductProcessor(imgPath))
                    .addPipeline(jdGoodsMapperPipeline)
                    .addUrl("https://list.jd.com/list.html?cat=670,677,678&page=0")
                    .thread(10)
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
