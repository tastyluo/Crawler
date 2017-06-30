package com.fun.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-28 11:39
 */
public class JDGoodsProcessor implements PageProcessor {

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    // 商品列表URL前缀
    private final static String URL_PREFIX = "https://list.jd.com/list.html\\?cat=";

    // CPU分类编号
    private final static String CPU_CAT_ID = "670,677,678";



    @Override
    public void process(Page page) {
        String pageNumStr = page.getHtml().xpath("//div[@id=J_searchWrap]/div[@class=f-pager]/div[@class=fp-text]/i/text()").get();
        int pageNum;
        if (pageNumStr != null) {
            pageNum = Integer.parseInt(pageNumStr);
            for (int i = 1; i < pageNum; i++) {
                page.addTargetRequest(URL_PREFIX + CPU_CAT_ID + "&page=" + i);
            }
        }
//        page.addTargetRequests(links);
        page.putField("title", page.getHtml().xpath("//div[@class='BlogEntity']/div[@class='BlogTitle']/h1").toString());
        page.putField("content", page.getHtml().$("div.content").toString());
        page.putField("tags",page.getHtml().xpath("//div[@class='BlogTags']/a/text()").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new JDGoodsProcessor())
                //从该页面开始抓取
                .addUrl(URL_PREFIX + CPU_CAT_ID)
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}
