package com.fun.pipeline;

import com.fun.entity.JdGoods;
import com.fun.mapper.JdGoodsMapper;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import javax.annotation.Resource;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-30 10:46
 * 持久化商品信息
 */
public class JDGoodsMapperPipeline implements PageModelPipeline<JdGoods> {

    @Resource
    private JdGoodsMapper jdGoodsMapper;

    @Override
    public void process(JdGoods jdGoods, Task task) {
        jdGoodsMapper.insert(jdGoods);
    }
}
