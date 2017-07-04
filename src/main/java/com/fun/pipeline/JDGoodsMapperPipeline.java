package com.fun.pipeline;

import com.fun.entity.JdGoods;
import com.fun.mapper.JdGoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-06-30 10:46
 * 持久化商品信息
 */
@Component("JDGoodsMapperPipeline")
public class JDGoodsMapperPipeline implements Pipeline {

    @Autowired
    private JdGoodsMapper jdGoodsMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        JdGoods jdGoods = resultItems.get("jdGoods");
        if (jdGoods != null) {
            JdGoods temp = new JdGoods();
            temp.setId(jdGoods.getId());
            JdGoods existGoods = jdGoodsMapper.selectOne(temp);
            if (existGoods != null) {
                jdGoodsMapper.updateByPrimaryKey(jdGoods);
            } else {
                jdGoodsMapper.insert(jdGoods);
            }
        }
    }
}
