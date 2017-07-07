package com.fun.webmagic.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fun.entity.JdGoods;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.xsoup.Xsoup;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-28 11:39
 */
public class JDGoodsProcessor extends RestTemplate implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(JDGoodsProcessor.class);

    private final static Map<String, JdGoods> goodsMap = new HashedMap();

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    // 商品列表URL前缀
    private final static String URL_PREFIX = "https://list.jd.com/list.html?cat=";

    // CPU分类编号
    private final static String CPU_CAT_ID = "670,677,678";

    // 价格请求地址
    private final static String PRICE_REQUEST_URL = "http://pm.3.cn/prices/pcpmgets?skuids=";

    // 评论请求地址
    private final static String COMMENTS_REQUEST_URL = "http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=";

    // 商品详细页url正则
    private final static String GOODS_DETAIL_URL_REGEX = "https://item\\.jd\\.com/[1-9]\\d*\\.html.*";

    private final static String SELF_OPERATED_TEXT = "自营";

    @Override
    public void process(Page page) {
        // 商品url
        List<String> itemUrls = page.getHtml()
                .css("li.gl-item div.j-sku-item div.p-img")
                .links()
                .regex("//item\\.jd\\.com/[1-9]\\d*\\.html")
                .replace("//item.jd.com", "https://item.jd.com")
                .all();

        page.addTargetRequests(itemUrls);

        if (page.getUrl().regex(GOODS_DETAIL_URL_REGEX).match()) {  // 商品详情页
            String pageHTML = page.getHtml().toString();
            String id = page.getUrl().toString()
                    .replaceAll("https://item\\.jd\\.com/", "")
                    .replaceAll("\\.html.*", "");
            String name = Xsoup.select(pageHTML, "//div[@class=product-intro]//div[@class=sku-name]/text()").get();
            String shopName = Xsoup.select(pageHTML,
                    "//div[@class=crumb-wrap]//div[@class=J-hove-wrap]//div[@class=name]/a/text()").get();
            String link = page.getUrl().toString();
            String selfOperated = Xsoup.select(pageHTML,
                    "//div[@class=crumb-wrap]//div[@class=J-hove-wrap]//div[@class=name]/[@class=u-jd]/text()").get();
            selfOperated = parseSelfOperated(selfOperated);
            BigDecimal price = getPrice(id);
            JSONObject comment = getComments(id);
            int commentsNum = (Integer) comment.get("commentsNum");
            BigDecimal goodRate = (BigDecimal) comment.get("goodRate");

            logger.info("[id: {}, name: {}, shopName: {}, price: {}, link: {}, commentsNum: {}, goodRate: {}, selfOperated: {}]",
                    id, name, shopName, price, link, commentsNum, goodRate, selfOperated);

            JdGoods jdGoods = new JdGoods();
            jdGoods.setGoodsId(id);
            jdGoods.setName(name);
            jdGoods.setShopName(shopName);
            jdGoods.setPrice(price);
            jdGoods.setCommentsNum(commentsNum);
            jdGoods.setLink(link);
            jdGoods.setGoodRate(goodRate);
            jdGoods.setSelfOperated(selfOperated);
            jdGoods.setCategory("cpu");
            goodsMap.put(id, jdGoods);
            page.putField("jdGoods", jdGoods);
        } else {    // 商品列表页
            // 发现url
            List<String> pageUrls = page.getHtml()
                    .css("span.p-num")
                    .links()
                    .regex("/list\\.html\\?cat=" + CPU_CAT_ID + "&page=[1-9]\\d*.*")
                    .replace("/list.html", "https://list.jd.com/list.html").all();
            page.addTargetRequests(pageUrls);
        }
    }

    /**
     * 转换自营
     * @param text
     * @return
     */
    public String parseSelfOperated(String text) {
        text = text != null ? text.trim() : "";
        return SELF_OPERATED_TEXT.equals(text) ? "1" : "0";
    }

    /**
     * 获取价格
     * @param goodsId 商品编号
     * @return String
     */
    public BigDecimal getPrice(String goodsId) {
        BigDecimal price = null;
        String url = PRICE_REQUEST_URL + goodsId + "&origin=2";
        JSONArray jsonResult = super.getForObject(url, JSONArray.class);
        if (jsonResult != null && jsonResult.size() > 0) {
            price = new BigDecimal(jsonResult.getJSONObject(0).get("op").toString());
        }
        return price;
    }

    /**
     * 获取评论数和好评率
     * @param goodsId 商品编号
     * @return JSONObject
     */
    public JSONObject getComments(String goodsId) {
        JSONObject comment = new JSONObject();
        String url = COMMENTS_REQUEST_URL + goodsId;
        String jsonResult = super.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonResult);
        if (jsonObject != null) {
            JSONObject commentObj = jsonObject.getJSONArray("CommentsCount").getJSONObject(0);
            comment.put("commentsNum", Integer.parseInt(commentObj.get("CommentCount").toString()));
            BigDecimal oneHundred = new BigDecimal("100");
            comment.put("goodRate", new BigDecimal(commentObj.get("GoodRate").toString())
                    .multiply(oneHundred)
                    .setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return comment;
    }

    @Override
    public Site getSite() {
        return site;
    }
}
