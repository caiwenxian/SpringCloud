package com.wenxianm.model.entity;


import lombok.Data;

/**
 * 歌手
 * @author caiwx
 * @date 2021/10/20 - 10:14
 **/
@Data
public class Artist extends BaseEntity {

    /**
     * 歌手id
     */
    private String artistId;

    /**
     * 名称
     */
    private String name;

    /**
     * 来源
     */
    private String origin;

    /**
     * 是否reptile热门歌曲
     */
    private Integer reptileHotSong;

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(String artistId, String name, String origin) {
        this.artistId = artistId;
        this.name = name;
        this.origin = origin;
    }

}
