package com.wenxianm.api;

import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.param.SongParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 歌曲相关api
 * @ClassName SongApi
 * @Author cwx
 * @Date 2021/9/24 17:34
 **/
@FeignClient("blog-main-service-song")
public interface SongApi {

    /**
     * 获取歌曲列表
     * @param songParam
     * @author caiwx
     * @date 2021/9/24 - 17:39
     * @return
     **/
    @GetMapping(value = "listSong")
    PageData<SongDto> listSong(SongParam songParam);
}
