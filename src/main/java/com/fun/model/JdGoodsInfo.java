package com.fun.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-05 15:39
 */
@Data
public class JdGoodsInfo {

    private String goodsId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 评论数
     */
    private Integer commentsNum;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 是否自营
     */
    private String selfOperated;

    /**
     * 商品链接
     */
    private String link;

    /**
     * 好评率
     */
    private BigDecimal goodRate;

    /**
     * 商品类型编号
     */
    private String category;
}
