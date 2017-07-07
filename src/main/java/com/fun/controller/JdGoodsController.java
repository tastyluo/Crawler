package com.fun.controller;

import com.fun.model.JdGoodsInfo;
import com.fun.service.JdGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-07 15:19
 */
@RestController
@RequestMapping("/jdGoods")
public class JdGoodsController {

    @Autowired
    private JdGoodsService jdGoodsService;

    @RequestMapping("/page")
    public List<JdGoodsInfo> listOfPage(@RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return jdGoodsService.listOfPage(pageNum, pageSize);
    }

}
