package com.wenxianm.controller;

import com.google.common.collect.Lists;
import com.wenxianm.annotation.ApiMethod;
import com.wenxianm.api.SongApi;
import com.wenxianm.model.Page;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.param.SongParam;
import com.wenxianm.service.song.ISongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName SongController
 * @Author cwx
 * @Date 2021/9/24 18:19
 **/
@RestController
@RequestMapping("/song")
@Slf4j
public class SongController implements SongApi{

    @Autowired
    ISongService songService;

    @ApiMethod
    @Override
    public PageData<SongDto> listSong(SongParam songParam) {
        return songService.listSong(songParam);
    }

    @Override
    public void reptileSong(String name) {
        songService.reptileSong(name);
    }

    @Override
    public void reptileMp3Url(@RequestBody List<Long> songIds) {
        songService.reptileMp3Url(songIds);
    }

    @Override
    public void reptileTopList() {
        songService.reptileTopList(Lists.newArrayList());
    }

    @Override
    public void reptileArtistHotSongs(Long artistId) {
        songService.reptileArtistHotSongs(artistId);
    }
}
