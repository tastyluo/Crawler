package com.fun.model;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-28 11:57
 * 京东商品
 */
@Data
@HelpUrl("https://search.jd.com/Search?keyword=cpu&enc=utf-8\\w+")
@TargetUrl("https://search.jd.com/Search?keyword=cpu&enc=utf-8\\w+")
@ExtractBy(value = "//li[@class=gl-item]", multi = true)
public class JDGoods {

    // 编号
    @ExtractBy("//div/@data-sku")
    private String id;

    // 名称
    @ExtractBy("./div/div[@class=p-name]/a/em/text()")
    private String name;

    // 店名
    @ExtractBy("./div/div[@class=p-shop]/@data-shop_name")
    private String shopName;

    // 评论数
    private String commentsNum;

    // 价格
    @ExtractBy("$('.w .itemInfo-wrap .summary-price.J-summary-price .p-price .price').text()")
    private String price;

    // 是否自营
    @ExtractBy("$('.J-hove-wrap.EDropdown.fr .item:eq(0) > .name > .u-jd').text()")
    private String selfOperated;

    // 商品链接
    @ExtractBy("./div/div[@class=p-img]/a/@href")
    private String link;

}
