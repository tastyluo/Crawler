package com.fun.schedule;

import com.fun.cache.CrawlerCache;
import com.fun.entity.JdGoods;
import com.fun.mapper.JdGoodsMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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
public class JobUpdater implements Job {

    @Autowired
    JdGoodsMapper jdGoodsMapper;

    @Autowired
    CrawlerCache crawlerCache;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String, JdGoods> cache = crawlerCache.getAll();
        if (cache.size() > 0) {
            List<String> idList = new ArrayList<>();
            for ( Map.Entry<String, JdGoods> entry : cache.entrySet()) {
                idList.add(entry.getKey());
            }

            Example example = new Example(JdGoods.class);
            example.createCriteria().andIn("goodsId", idList);
            List<JdGoods> existGoods = jdGoodsMapper.selectByExample(example);

            List<JdGoods> insertList = new ArrayList<>();
            for ( Map.Entry<String, JdGoods> entry : cache.entrySet()) {
                boolean exist = false;
                String key = entry.getKey();
                JdGoods item = entry.getValue();
                for (int i = 0; i < existGoods.size(); i++) {
                    JdGoods jdGoods = existGoods.get(i);
                    if (jdGoods.getGoodsId().equals(key)) {
                        exist = true;
                    }
                }
                if (exist) {
                    jdGoodsMapper.updateByPrimaryKey(item);
                } else {
                    insertList.add(item);
                }
            }
            jdGoodsMapper.insertList(insertList);
        }
    }
}
