package com.wenxianm.model.entity;


import lombok.Data;

import javax.persistence.Table;

/**
 * 歌手
 * @author caiwx
 * @date 2021/10/20 - 10:14
 **/
@Data
@Table(name = "t_artist")
public class Artist extends BaseEntity {

    /**
     * 歌手id
     */
    private Long artistId;

    /**
     * 名称
     */
    private String name;

    /**
     * 来源
     * @see com.wenxianm.model.enums.SongOriginEnum
     */
    private Integer origin;

    /**
     * 是否reptile热门歌曲
     */
    private Integer reptileHotSong;

    /**
     * 头像
     */
    private String photo;

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    public Artist(Long artistId, String name, Integer origin) {
        this.artistId = artistId;
        this.name = name;
        this.origin = origin;
    }

}
