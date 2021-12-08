package com.wenxianm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ClassName SongLyric
 * @Author cwx
 * @Date 2021/12/7 15:46
 **/
@Data
@AllArgsConstructor
@Table(name = "t_song_lyric")
public class SongLyric extends BaseEntity{

    /**
     * 歌词
     */
    @Column(name = "lyric")
    private String lyric;

    /**
     * 歌曲id
     */
    @Column(name = "song_id")
    private Long songId;

    /**
     * 歌词版本
     */
    @Column(name = "version")
    private Integer version;

    /**
     * 是否有歌词
     */
    @Column(name = "has_lyric")
    private Integer hasLyric;

    public SongLyric() {
    }
}
