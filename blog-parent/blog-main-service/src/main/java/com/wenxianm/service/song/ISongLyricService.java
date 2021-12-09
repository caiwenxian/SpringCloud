package com.wenxianm.service.song;

import com.wenxianm.model.entity.SongLyric;
import com.wenxianm.model.vo.SongLyricVO;

import java.util.List;

/**
 * 歌词业务
 * @ClassName ISongLyricService
 * @Author cwx
 * @Date 2021/12/7 15:50
 **/
public interface ISongLyricService {

    /**
     * 保存单个
     * @param songLyric
     * @author caiwx
     * @date 2021/12/7 - 15:53
     **/
    void saveOne(SongLyric songLyric);

    /**
     * 保存多个
     * @param songLyrics
     * @author caiwx
     * @date 2021/12/7 - 15:53
     **/
    void saveBatch(List<SongLyric> songLyrics);

    /**
     * 批量更新
     * @param songLyrics
     * @author caiwx
     * @date 2021/12/7 - 15:55
     **/
    void updateBatch(List<SongLyric> songLyrics);

    /**
     * 获取单个
     * @param id 主键
     * @author caiwx
     * @date 2021/12/7 - 16:00
     * @return SongLyric
     **/
    SongLyric getOne(String id);

    /**
     * 获取列表
     * @param ids 主键集合
     * @author caiwx
     * @date 2021/12/7 - 16:00
     * @return SongLyric
     **/
    List<SongLyric> list(List<String> ids);

    /**
     * 根据歌曲id获取歌词
     * @param songIds 歌曲id集合
     * @author caiwx
     * @date 2021/12/8 - 11:37
     * @return List<SongLyricVO>
     **/
    List<SongLyricVO> listBySongIds(List<Long> songIds);

    /**
     * reptile歌词
     * @author caiwx
     * @date 2021/12/7 - 17:00
     **/
    void reptileLyric();
}
