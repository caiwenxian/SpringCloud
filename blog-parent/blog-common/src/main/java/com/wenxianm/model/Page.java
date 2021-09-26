package com.wenxianm.model;

import java.io.Serializable;

/**
 * @ClassName Page
 * @Author cwx
 * @Date 2021/9/24 18:11
 **/
public class Page implements Serializable {

    private static final long serialVersionUID = 1489126601690028478L;

    /**
     * 页码
     */
    private int pageIndex;
    /**
     * 页大小
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private int totalItem;

}
