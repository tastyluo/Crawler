package com.fun.webmagic.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fun.entity.JdGoods;
import com.fun.util.ImageDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-28 11:39
 */
public class JDProductProcessor extends RestTemplate implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(JDProductProcessor.class);

    // 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    // 商品列表URL前缀
    private static final String URL_PREFIX = "https://list.jd.com/list.html?cat=";

    // CPU分类编号
    private static final String CPU_CAT_ID = "670,677,678";

    // 价格请求地址
    private static final String PRICE_REQUEST_URL = "http://pm.3.cn/prices/pcpmgets?skuids=";

    // 评论请求地址
    private static final String COMMENTS_REQUEST_URL = "http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=";

    // 商品详细页url正则
    private static final String GOODS_DETAIL_URL_REGEX = "https://item\\.jd\\.com/[1-9]\\d*\\.html.*";

    private static final String SELF_OPERATED_TEXT = "自营";

    private static final Integer PAGE_SIZE = 60;

    private String imgSavePath;

    public JDProductProcessor(String imgPath) {
        this.imgSavePath = imgPath;
    }

    @Override
    public void process(Page page) {
        String pageHTML = page.getHtml().toString();
        if (page.getUrl().regex(GOODS_DETAIL_URL_REGEX).match()) {  // 商品详情页
            Map<String, String> paramsMap = getParams(page.getUrl().toString());
            int pageNum = Integer.parseInt(paramsMap.get("page"));
            int pageOrder = Integer.parseInt(paramsMap.get("order"));
            int saleOrder = (pageNum - 1) * PAGE_SIZE + pageOrder;
            String id = page.getUrl().toString()
                    .replaceAll("https://item\\.jd\\.com/", "")
                    .replaceAll("\\.html.*", "");
            String name = Xsoup.select(pageHTML, "//div[@class=product-intro]//div[@class=sku-name]/text()").get();
            String shopName = Xsoup.select(pageHTML,
                    "//div[@class=crumb-wrap]//div[@class=J-hove-wrap]//div[@class=name]/a/text()").get();
            String link = page.getUrl().toString();
            String selfOperated = Xsoup.select(pageHTML,
                    "//div[@class=crumb-wrap]//div[@class=J-hove-wrap]//div[@class=name]/[@class=u-jd]/text()").get();
            String imgUrl = Xsoup.select(pageHTML,
                    "//div[@class=w]//div[@class=preview]//img[@id=spec-img]/@data-origin").get();
            try {
                imgUrl = "http:" + imgUrl;
                ImageDownloader.download(imgUrl, id , imgSavePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            jdGoods.setSaleOrder(saleOrder);
            page.putField("jdGoods", jdGoods);
        } else {    // 商品列表页
            String pageNum = Xsoup.select(pageHTML, "//span[@class=p-num]/a[@class=curr]/text()").get();
            // 商品url
            List<String> itemUrls = page.getHtml()
                    .xpath("//li[@class=gl-item]//div[@class=j-sku-item][1]//div[@class=p-img]")
                    .links()
                    .regex("//item\\.jd\\.com/[1-9]\\d*\\.html")
                    .replace("//item.jd.com", "https://item.jd.com")
                    .all();

            // 给商品url添加排序信息 （按照销量排序）
            List<String> parseItemUrls = new ArrayList<>();
            for (int i = 0; i < itemUrls.size(); i++) {
                parseItemUrls.add(itemUrls.get(i) + "?page=" + pageNum + "&order=" + (i + 1));
            }

            page.addTargetRequests(parseItemUrls);
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
     * 提取url的参数
     * @param url
     * @return
     */
    public Map<String, String> getParams(String url) {
        String[] strArr = url.split("[?]");
        Map<String, String> params = new HashMap<>();
        if (strArr.length > 1) {
            String[] items = strArr[1].split("&");
            for (int i = 0; i < items.length; i++) {
                String[] tempParam = items[i].split("=");
                params.put(tempParam[0], tempParam[1]);
            }
        }
        return params;
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
            price = new BigDecimal(jsonResult.getJSONObject(0).get("p").toString());
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
        comment.put("commentsNum", 0);
        comment.put("goodRate", null);
        String url = COMMENTS_REQUEST_URL + goodsId;
        try {
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
        } catch (RestClientException e) {
            StringWriter sw = new StringWriter();
            sw.write("comments api request error");
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
        }

        return comment;
    }

    @Override
    public Site getSite() {
        return site;
    }
}
