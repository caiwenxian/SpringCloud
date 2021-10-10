package com.wenxianm.dao.song;

import com.wenxianm.model.entity.Song;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName ISongDao
 * @Author cwx
 * @Date 2021/10/9 16:03
 **/
@Repository
public interface ISongDao extends Mapper<Song> {
}
