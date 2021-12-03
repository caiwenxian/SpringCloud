package com.wenxianm.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PageData
 * @Author cwx
 * @Date 2021/9/24 18:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<E> implements Serializable {

    private static final long serialVersionUID = 8148912660169002849L;

    private List<E> list;

    private Page page;

    public static PageData emptyPage() {
        PageData pageData = new PageData();
        pageData.setList(Lists.newArrayList());
        return pageData;
    }

    public static PageData emptyPage(Page page) {
        PageData pageData = new PageData();
        pageData.setList(Lists.newArrayList());
        pageData.setPage(new Page(page));
        return pageData;
    }
}
