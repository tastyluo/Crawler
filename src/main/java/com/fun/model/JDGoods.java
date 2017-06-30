package com.fun.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-28 11:57
 * 京东商品
 */
@Data
@ExtractBy(value = "//li[@class=gl-item]", multi = true)
public class JDGoods extends RestTemplate implements AfterExtractor {

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
    private String selfOperated;

    // 商品链接
    @ExtractBy("//div[@class=j-sku-item]/div[@class=p-img]/a/@href")
    private String link;

    private final static String PRICE_REQUEST_URL = "http://pm.3.cn/prices/pcpmgets?skuids=";

    private final static String COMMENTS_REQUEST_URL = "http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=";

    @Override
    public void afterProcess(Page page) {
        setPriceByJson();
        setCommentsByJson();
        // 发现url
        List<String> urls = page.getHtml()
                .css("span.p-num")
                .links()
                .regex(".*/list.html\\?cat=.*&page=[1-9]\\d*.*").all();
        page.addTargetRequests(urls);

    }

    public void setPriceByJson() {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json;charset=utf-8");
        JSONArray jsonResult = super.getForObject(PRICE_REQUEST_URL + id + "&origin=2", JSONArray.class, params);
        if (jsonResult != null && jsonResult.size() > 0) {
            this.price = new BigDecimal(jsonResult.getJSONObject(0).get("op").toString());
            System.out.println("价格: " + this.price);
        }
    }

    public void setCommentsByJson() {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json;charset=utf-8");
        JSONObject jsonResult = super.getForObject(COMMENTS_REQUEST_URL + id, JSONObject.class, params);
        if (jsonResult != null) {
            JSONArray jsonArray = jsonResult.getJSONArray("CommentsCount");
            this.commentsNum = Integer.parseInt(jsonArray.getJSONObject(0).get("CommentCount").toString());
            this.goodRate = new BigDecimal(jsonArray.getJSONObject(0).get("GoodRate").toString());
            System.out.println("评论数: " + this.price);
            System.out.println("好评率: " + this.price);
        }
    }

    public static void main (String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000).setRetryTimes(3)
                , new ConsolePageModelPipeline(), JDGoods.class)
                .addUrl("https://list.jd.com/list.html?cat=670,677,678&page=0").thread(5).run();
    }
}
