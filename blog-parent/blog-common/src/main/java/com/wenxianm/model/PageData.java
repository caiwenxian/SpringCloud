package com.wenxianm.model;

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
}
