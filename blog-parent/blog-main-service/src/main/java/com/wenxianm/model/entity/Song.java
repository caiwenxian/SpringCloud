package com.wenxianm.model.entity;

import lombok.Data;

/**
 * @ClassName Song
 * @Author cwx
 * @Date 2021/10/9 16:07
 **/
@Data
public class Song extends BaseEntity {


    /**
     * 歌曲id
     */
    private String songId;

    /**
     * 名称
     */
    private String name;

    /**
     * 歌手id
     */
    private String artistId;

    /**
     * 歌词id
     */
    private String lyricId;

    /**
     * map3Url
     */
    private String mp3Url;

    /**
     * 来源
     */
    private String origin;

    /**
     * 序号
     */
    private Integer num;
}
