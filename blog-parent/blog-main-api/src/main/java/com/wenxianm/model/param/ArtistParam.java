package com.wenxianm.model.param;

import com.wenxianm.model.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 歌手请求参数
 * @ClassName SongParam
 * @Author cwx
 * @Date 2021/9/24 17:40
 **/
@Data
@AllArgsConstructor
public class ArtistParam extends Page {

    /** 名称 */
    private String name;

    private Integer reptileHotSong;

    public ArtistParam(String name) {
        this.name = name;
    }
}
