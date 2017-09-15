package com.fun.schedule;

import com.fun.cache.CrawlerCache;
import com.fun.entity.JdGoods;
import com.fun.mapper.JdGoodsMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-06 11:12
 */
@Service
public class DataUpdater implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataUpdater.class);

    @Autowired
    JdGoodsMapper jdGoodsMapper;

    @Autowired
    CrawlerCache crawlerCache;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String, JdGoods> cache = crawlerCache.getAll();
        if (cache.size() > 0) {
            List<String> idList = new ArrayList<>();
            List<JdGoods> jdGoodsList = new ArrayList<>();
            for ( Map.Entry<String, JdGoods> entry : cache.entrySet()) {
                idList.add(entry.getKey());
                jdGoodsList.add(entry.getValue());
            }
            Example example = new Example(JdGoods.class);
            example.createCriteria().andIn("goodsId", idList);
            int deleteCount = jdGoodsMapper.deleteByExample(example);
            jdGoodsMapper.batchInsert(jdGoodsList);
        }
    }
}
