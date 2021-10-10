package com.wenxianm.service.song.impl;

import com.wenxianm.dao.song.ISongDao;
import com.wenxianm.model.Page;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.entity.Song;
import com.wenxianm.model.param.SongParam;
import com.wenxianm.service.song.ISongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @ClassName SongServiceImpl
 * @Author cwx
 * @Date 2021/10/9 16:28
 **/
@Service
@Slf4j
public class SongServiceImpl implements ISongService {

    @Autowired
    ISongDao songDao;

    @Override
    public PageData<SongDto> listSong(SongParam songParam, Page page) {
        Example example = new Example(Song.class);
        example.createCriteria().andLike(,songParam.getName());
        songDao.selectCountByExample(example);
        return null;
    }
}
