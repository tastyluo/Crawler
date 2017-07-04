package com.fun.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fun.util.StringCompareFormatter;
import lombok.Data;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.Formatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-28 11:57
 * 京东商品
 */
@Data
@ExtractBy(value = "//li[@class=gl-item]", multi = true)
public class JDGoodsInfo extends RestTemplate implements AfterExtractor {

    // 编号
    @ExtractBy("//div[@class=j-sku-item]/@data-sku")
    private String id;

    // 名称
    @ExtractBy("//div[@class=j-sku-item]/div[@class=p-name]/a/em/text()")
    private String name;

    // 店名
    @ExtractBy("//div[@class=j-sku-item]/div[@class=p-shop]/@data-shop_name")
    private String shopName;

    // 评论数
    private Integer commentsNum;

    // 好评率
    private BigDecimal goodRate;

    // 价格
    private BigDecimal price;

    // 是否自营
    @Formatter(value = "自营", formatter = StringCompareFormatter.class)
    @ExtractBy("//div[@class=j-sku-item]/i[@id=js-jdzy]/text()")
    private String selfOperated;

    // 商品链接
    @ExtractBy("//div[@class=j-sku-item]/div[@class=p-img]/a/@href")
    private String link;

    private final static String PRICE_REQUEST_URL = "http://pm.3.cn/prices/pcpmgets?skuids=";

    private final static String COMMENTS_REQUEST_URL = "http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=";

    @Override
    public void afterProcess(Page page) {
        setPriceAfterPage();
        setCommentsAfterPage();
        // 发现url
        List<String> urls = page.getHtml()
                .css("span.p-num")
                .links()
                .regex("/list.html\\?cat=670,677,678&page=[1-9]\\d*.*")
                .replace("/list.html", "https://list.jd.com/list.html").all();
        List<String> filterUrls = new ArrayList<>();
        for (String url : urls) {
            if (url.contains("670,677,678")) {
                filterUrls.add(url);
            }
        }
        page.addTargetRequests(filterUrls);
    }

    /**
     * 从请求json中获取 商品价格
     */
    public void setPriceAfterPage() {
        String url = PRICE_REQUEST_URL + id + "&origin=2";
        JSONArray jsonResult = super.getForObject(url, JSONArray.class);
        if (jsonResult != null && jsonResult.size() > 0) {
            this.price = new BigDecimal(jsonResult.getJSONObject(0).get("op").toString());
        }
    }

    /**
     * 从请求json中获取 评论数 和 好评率
     */
    public void setCommentsAfterPage() {
        String url = COMMENTS_REQUEST_URL + id;
        String jsonResult = super.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonResult);
        if (jsonObject != null) {
            JSONObject commentObj = jsonObject.getJSONArray("CommentsCount").getJSONObject(0);
            this.commentsNum = Integer.parseInt(commentObj.get("CommentCount").toString());
            BigDecimal oneHundred = new BigDecimal("100");
            this.goodRate = new BigDecimal(commentObj.get("GoodRate").toString()).multiply(oneHundred);
        }
    }

    public static void main(String args[]) {
        OOSpider.create(Site.me().setSleepTime(1000).setRetryTimes(3)
                , new ConsolePageModelPipeline(), JDGoodsInfo.class)
                .addUrl("https://list.jd.com/list.html?cat=670,677,678&page=0").thread(5).run();
    }
}
