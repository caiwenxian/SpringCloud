package com.wenxianm.api;

import com.google.common.collect.Lists;
import com.wenxianm.model.Page;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.param.SongParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 歌曲相关api
 * @ClassName SongApi
 * @Author cwx
 * @Date 2021/9/24 17:34
 **/
@Component
@FeignClient(name = "blog-main-service", path = "/blog/song")
public interface SongApi {

    /**
     * 获取歌曲列表
     * @param songParam
     * @param page
     * @author caiwx
     * @date 2021/9/24 - 17:39
     * @return
     **/
    @GetMapping(value = "listSong")
    PageData<SongDto> listSong(SongParam songParam);

    /**
     * 根据名称爬取歌曲
     * @param name 歌曲名称
     * @author caiwx
     * @date 2021/10/12 - 17:47
     **/
    @GetMapping(value = "/reptileSong")
    void reptileSong(@RequestParam String name);

    /**
     * 获取歌曲的mp3
     * @param songIds 歌曲id
     * @author caiwx
     * @date 2021/10/14 - 11:25
     **/
    @PostMapping(value = "/reptileMp3Url")
    void reptileMp3Url(@RequestBody List<Long> songIds);

    /**
     * reptile歌曲排行榜
     * @param urls 榜单url
     * @author caiwx
     * @date 2021/10/19 - 17:10
     **/
    @PostMapping(value = "/reptileTopList")
    void reptileTopList(@RequestBody List<String> urls);

    /**
     * reptile歌手热门歌曲
     * @param artistId 歌手id
     * @author caiwx
     * @date 2021/10/20 - 16:06
     **/
    @GetMapping(value = "/reptileArtistHotSongs")
    void reptileArtistHotSongs(@RequestParam Long artistId);

    /**
     * 更新歌曲mp3
     * @param songDto
     * @author caiwx
     * @date 2021/11/20 - 16:01
     **/
    @PostMapping(value = "/updateMp3Url")
    void updateMp3Url(@RequestBody SongDto songDto);

    /**
     * reptile歌词
     * @author caiwx
     * @date 2021/11/20 - 16:01
     **/
    @GetMapping(value = "/reptileLyric")
    void reptileLyric();

}
