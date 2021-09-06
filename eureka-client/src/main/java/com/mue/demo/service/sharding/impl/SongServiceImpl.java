package com.mue.demo.service.sharding.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mue.demo.dao.sharding.OrderDao;
import com.mue.demo.dao.sharding.SongDao;
import com.mue.demo.model.sharding.OrderEntity;
import com.mue.demo.model.sharding.Song;
import com.mue.demo.service.sharding.ISongService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SongServiceImpl
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/31 16:59
 * @Version 1.0
 **/
@Service
public class SongServiceImpl extends ServiceImpl<SongDao, Song> implements ISongService {


    @Override
    public void saveOne(Song song) {
        this.save(song);
    }

    @Override
    public List<Song> listAll() {
        return this.list();
    }
}
