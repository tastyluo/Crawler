package com.fun.controller;

import com.fun.service.JdGoodsService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-07 15:19
 */
@RestController
@RequestMapping("/jdGoods")
@PreAuthorize("hasRole('USER')")
public class JdGoodsController {

    @Autowired
    private JdGoodsService jdGoodsService;

    @RequestMapping(value = "/page", produces = "application/json;charset=UTF-8")
    public PageInfo listOfPage(@RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return jdGoodsService.listOfPage(pageNum, pageSize);
    }

}
