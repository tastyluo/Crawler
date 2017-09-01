package com.fun.common;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-09-01 11:00
 */

import tk.mybatis.mapper.common.RowBoundsMapper;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.condition.SelectByConditionMapper;
import tk.mybatis.mapper.common.condition.SelectCountByConditionMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;

public interface MySelectMapper<T> extends
        BaseSelectMapper<T>,
        SelectByExampleMapper<T>,
        SelectCountByExampleMapper<T>,
        SelectByConditionMapper<T>,
        SelectCountByConditionMapper<T>,
        RowBoundsMapper<T> {
}
