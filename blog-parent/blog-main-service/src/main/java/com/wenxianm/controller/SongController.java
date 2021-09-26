package com.wenxianm.controller;

import com.wenxianm.annotation.ApiMethod;
import com.wenxianm.api.SongApi;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.param.SongParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class SongController implements SongApi {

    @ApiMethod
    @Override
    public PageData<SongDto> listSong(SongParam songParam) {
        log.info("进入listSong");
        List<SongDto> list = Collections.singletonList(new SongDto("歌曲1"));
        PageData<SongDto> pageData = new PageData<>();
        pageData.setList(list);
        return pageData;
    }
}
