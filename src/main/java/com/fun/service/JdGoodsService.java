package com.fun.service;

import com.fun.entity.JdGoods;
import com.fun.mapper.JdGoodsMapper;
import com.fun.model.JdGoodsInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-06 14:06
 */
@Service
public class JdGoodsService {

    @Autowired
    private JdGoodsMapper jdGoodsMapper;

    public PageInfo listOfPage(int pageNum, int pageSize) {
        Example example = new Example(JdGoods.class);
        example.setOrderByClause("COMMENTS_NUM DESC");
        Page<JdGoodsInfo> goodsPage = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(()-> jdGoodsMapper.selectByExample(example));
        PageInfo page = new PageInfo(goodsPage);
        return page;
    }

}
