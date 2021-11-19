package com.wenxianm.dao;

import com.wenxianm.model.entity.Artist;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * 歌手
 * @ClassName IArtistDao
 * @Author cwx
 * @Date 2021/10/20 10:40
 **/
@Repository
public interface IArtistDao extends Mapper<Artist> {
}
