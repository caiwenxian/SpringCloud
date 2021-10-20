package com.wenxianm.model.entity;

import lombok.Data;

import javax.persistence.Table;

/**
 * @ClassName Song
 * @Author cwx
 * @Date 2021/10/9 16:07
 **/
@Data
@Table(name = "t_song")
public class Song extends BaseEntity {


    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * 名称
     */
    private String name;

    /**
     * 歌手id
     */
    private Long artistId;

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

    public Song() {}

    public Song(Long songId, String name) {
        this.songId = songId;
        this.name = name;
    }

    public Song(Long songId, String name, Long artistId) {
        this.songId = songId;
        this.name = name;
        this.artistId = artistId;
    }
}
