package com.fun.util;

import us.codecraft.webmagic.model.formatter.ObjectFormatter;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-02 22:29
 */
public class StringCompareFormatter implements ObjectFormatter<String> {

    private String compare;

    @Override
    public String format(String text) throws Exception {
        return compare.equals(text) ? "1" : "0";
    }

    @Override
    public Class<String> clazz() {
        return String.class;
    }

    @Override
    public void initParam(String[] strings) {
        compare = strings[0];
    }
}
