package com.fun.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-28 11:39
 */
public class GithubRepoPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setDomain("search.jd.com").setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        List<String> links = page.getHtml().links().regex("https://search.jd.com/Search?keyword=cpu&enc=utf-8").all();
        page.addTargetRequests(links);
        page.putField("title", page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
        page.putField("content", page.getHtml().$("div.content").toString());
        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new GithubRepoPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://search.jd.com/Search?keyword=cpu&enc=utf-8")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
