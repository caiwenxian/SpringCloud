package com.wenxianm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SongLyric
 * @Author cwx
 * @Date 2021/12/7 15:46
 **/
@Data
@AllArgsConstructor
public class SongLyricVO implements Serializable {

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 歌词
     */
    private String lyric;

    /**
     * 歌词版本
     */
    private Integer version;

    public SongLyricVO() {
    }
}
