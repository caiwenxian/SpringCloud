package com.wenxianm.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Page
 * @Author cwx
 * @Date 2021/9/24 18:11
 **/
@Data
public class Page implements Serializable {

    private static final long serialVersionUID = 1489126601690028478L;

    /**
     * 页码
     */
    private int pageIndex = 1;
    /**
     * 页大小
     */
    private int pageSize = 10;
    /**
     * 总记录数
     */
    private int totalItem = 0;

    public int getOffset() {
        return (this.pageIndex - 1) * pageSize;
    }

    public int getLimit() {
        return this.pageSize;
    }

    public Page() {
    }

    public Page(Page page) {
        this.pageIndex = page.getPageIndex();
        this.pageSize = page.getPageSize();
        this.totalItem = page.getTotalItem();
    }
}
