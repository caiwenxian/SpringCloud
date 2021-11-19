package com.wenxianm.model.param;

import com.wenxianm.model.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 歌曲搜索相关
 * @ClassName SongSearchParam
 * @Author cwx
 * @Date 2021/11/17 11:22
 **/
@Data
public class SongSearchParam extends Page implements Serializable {

    /**
     * 歌曲id
     */
    private List<Long> songIds;

    /**
     * 搜索key
     */
    private String searchKey;

    /**
     * 歌手id
     */
    private Long artistId;


}
