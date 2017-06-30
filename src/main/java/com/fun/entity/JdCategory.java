package com.fun.entity;

import javax.persistence.*;

@Table(name = "JD_CATEGORY")
public class JdCategory {
    /**
     * 分类编号
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分类名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 父类别编号
     */
    @Column(name = "PARENT_ID")
    private Integer parentId;

    /**
     * 类别路径
     */
    @Column(name = "PATH")
    private String path;

    /**
     * 层次深度
     */
    @Column(name = "DEPTH")
    private String depth;

    /**
     * 获取分类编号
     *
     * @return ID - 分类编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置分类编号
     *
     * @param id 分类编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分类名称
     *
     * @return NAME - 分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名称
     *
     * @param name 分类名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取父类别编号
     *
     * @return PARENT_ID - 父类别编号
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父类别编号
     *
     * @param parentId 父类别编号
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取类别路径
     *
     * @return PATH - 类别路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置类别路径
     *
     * @param path 类别路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取层次深度
     *
     * @return DEPTH - 层次深度
     */
    public String getDepth() {
        return depth;
    }

    /**
     * 设置层次深度
     *
     * @param depth 层次深度
     */
    public void setDepth(String depth) {
        this.depth = depth;
    }
}