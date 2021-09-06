package com.mue.demo.service.sharding;


import com.mue.demo.model.sharding.OrderEntity;
import com.mue.demo.model.sharding.Song;

import java.util.List;

/**
 * @ClassName ISongService
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/25 17:46
 * @Version 1.0
 **/
public interface ISongService extends IBaseService<Song>{

    /**
     * save
     * @param song
     */
    void saveOne(Song song);

    /**
     * listAll
     * @return
     */
    List<Song> listAll();
}
