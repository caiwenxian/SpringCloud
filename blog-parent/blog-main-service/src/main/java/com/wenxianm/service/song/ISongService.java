package com.wenxianm.service.song;

import com.wenxianm.model.Page;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.entity.Song;
import com.wenxianm.model.param.SongParam;
import com.wenxianm.model.vo.SongSearchVO;

import java.util.List;

/**
 * @ClassName ISongService
 * @Author cwx
 * @Date 2021/10/9 16:21
 **/
public interface ISongService {

    /**
     * 获取歌曲列表
     * @param page
     * @param songParam
     * @author caiwx
     * @date 2021/10/9 - 16:25
     * @return PageData<SongDto>
     **/
    PageData<SongDto> listSong(SongParam songParam);

    /**
     * 获取歌曲列表
     * @param songParam
     * @author caiwx
     * @date 2021/11/3 - 16:31
     * @return List<SongDto>
     **/
    List<SongDto> list(SongParam songParam);

    /**
     * 获取30条没有mp3的歌曲
     * @author caiwx
     * @date 2021/10/15 - 10:58
     * @return List<Song>
     **/
    List<Song> listSongNoMp3();

    /**
     * 根据歌曲id获取歌曲
     * @param songId 歌曲id
     * @author caiwx
     * @date 2021/10/13 - 16:46
     * @return SongDto
     **/
    SongDto getBySongId(Long songId);

    /**
     * 新增歌曲
     * @param song
     * @author caiwx
     * @date 2021/10/12 - 18:02
     **/
    void addSong(Song song);

    /**
     * 更新歌曲
     * @param song
     * @author caiwx
     * @date 2021/10/13 - 18:16
     **/
    void updateSong(Song song);


    /**
     * 根据歌曲名称爬取歌曲
     * @param name 歌曲名称
     * @author caiwx
     * @date 2021/10/12 - 17:49
     **/
    void reptileSong(String name);

    /**
     * 根据歌曲id获取歌曲的播放url
     * @param songIds 歌曲id
     * @author caiwx
     * @date 2021/10/13 - 18:05
     **/
    void reptileMp3Url(List<Long> songIds);

    /**
     * reptile歌曲排行榜
     * @param topUrls 排行榜url
     * @author caiwx
     * @date 2021/10/19 - 11:46
     **/
    void reptileTopList(List<String> topUrls);

    /**
     * reptile歌手的热门歌曲
     * @param artistId 歌手id
     * @author caiwx
     * @date 2021/10/19 - 11:46
     **/
    void reptileArtistHotSongs(Long artistId);

    /**
     * reptile歌手的热门歌曲
     * @param artistId
     * @author caiwx
     * @date 2021/10/28 - 10:58
     **/
    void reptileHotSongs(Long artistId);

    /**
     * 搜索歌曲或者歌手
     * @param name
     * @author caiwx
     * @date 2021/11/3 - 16:28
     * @return SongSearchVO
     **/
    SongSearchVO search(String name);
}
