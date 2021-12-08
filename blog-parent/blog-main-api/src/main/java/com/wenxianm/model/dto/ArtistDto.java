package com.wenxianm.model.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 歌手
 * @author caiwx
 * @date 2021/10/20 - 10:14
 **/
@Data
public class ArtistDto implements Serializable {

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
     * photo
     */
    private String photo;

    public ArtistDto() {
    }

    public ArtistDto(String name) {
        this.name = name;
    }

    public ArtistDto(Long artistId, String name, Integer origin) {
        this.artistId = artistId;
        this.name = name;
        this.origin = origin;
    }

}
