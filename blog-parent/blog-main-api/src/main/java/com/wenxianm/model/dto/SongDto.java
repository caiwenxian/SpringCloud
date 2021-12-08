package com.wenxianm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName SongDto
 * @Author cwx
 * @Date 2021/9/24 18:16
 **/
@Data
public class SongDto {

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
     * mp3Url
     */
    private String mp3Url;

    /**
     * cover
     */
    private String cover;

    public SongDto() {
    }

    public SongDto(String name) {
        this.name = name;
    }
}
