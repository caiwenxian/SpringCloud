package com.wenxianm.service.song;

import com.wenxianm.model.Page;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.param.SongParam;

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
    PageData<SongDto> listSong(SongParam songParam, Page page);
}
