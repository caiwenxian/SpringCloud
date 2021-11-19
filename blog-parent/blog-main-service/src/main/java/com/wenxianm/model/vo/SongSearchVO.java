package com.wenxianm.model.vo;

import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.ArtistDto;
import com.wenxianm.model.dto.SongDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索结果
 * @ClassName SongSearchVO
 * @Author cwx
 * @Date 2021/11/3 16:25
 **/
@Data
@AllArgsConstructor
public class SongSearchVO implements Serializable {

    /**
     * 歌曲
     */
    private PageData<SongDto> songs;

    /**
     * 歌手
     */
    private List<ArtistDto> artists;

    public SongSearchVO() {
    }
}
