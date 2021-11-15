package com.wenxianm.model.vo;

import lombok.Data;

/**
 * @ClassName SongDto
 * @Author cwx
 * @Date 2021/9/24 18:16
 **/
@Data
public class SongHotVO {

    /** 歌曲id */
    private Long songId;

    /** 名称 */
    private String name;

    /**
     * 歌手id
     */
    private Long artistId;

    /**
     * 歌手id
     */
    private String artistName;

    /**
     * 播放次数
     */
    private Integer playTimes;



    public SongHotVO() {
    }

    public SongHotVO(String name) {
        this.name = name;
    }
}
