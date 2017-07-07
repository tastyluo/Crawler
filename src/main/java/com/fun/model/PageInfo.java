package com.fun.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 版本： 1.0
 * 创建人： 罗佳星
 * 创建时间：2017-07-07 14:58
 */
@Data
public class PageInfo {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private Integer page = 1;

    @Transient
    private Integer rows = 10;
}
