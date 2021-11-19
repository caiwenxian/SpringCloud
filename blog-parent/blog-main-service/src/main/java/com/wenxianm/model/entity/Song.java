package com.wenxianm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ClassName Song
 * @Author cwx
 * @Date 2021/10/9 16:07
 **/
@Data
@AllArgsConstructor
@Table(name = "t_song")
public class Song extends BaseEntity {


    /**
     * 歌曲id
     */
    @Column(name = "song_id")
    private Long songId;

    /**
     * 名称
     */
    private String name;

    /**
     * 歌手id
     */
    @Column(name = "artist_id")
    private Long artistId;

    /**
     * 歌词id
     */
    @Column(name = "lyric_id")
    private String lyricId;

    /**
     * map3Url
     */
    @Column(name = "mp3_url")
    private String mp3Url;

    /**
     * 来源
     * @see com.wenxianm.model.enums.SongOriginEnum
     */
    private Integer origin;

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

    public Song(Long songId, String name, Long artistId, Integer origin) {
        this.songId = songId;
        this.name = name;
        this.artistId = artistId;
        this.origin = origin;
    }

    public Song(Long songId, String name, Long artistId, Integer origin, Integer num) {
        this.songId = songId;
        this.name = name;
        this.artistId = artistId;
        this.origin = origin;
        this.num = num;
    }
}
