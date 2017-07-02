package com.fun.listener;

import com.fun.job.JobCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-02 22:58
 */
public class CrawlerStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private JobCrawler jobCrawler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        jobCrawler.crawl();
    }
}