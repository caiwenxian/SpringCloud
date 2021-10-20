package com.wenxianm.model.param;

import com.wenxianm.model.Page;
import lombok.Data;

/**
 * 歌曲请求参数
 * @ClassName SongParam
 * @Author cwx
 * @Date 2021/9/24 17:40
 **/
@Data
public class SongParam extends Page {

    /** 名称 */
    private String name;
}
