package com.fun.mapper;

import com.fun.common.MyMapper;
import com.fun.entity.JdGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JdGoodsMapper extends MyMapper<JdGoods> {
    int batchInsert(@Param("goodsList") List<JdGoods> goodsList);
}