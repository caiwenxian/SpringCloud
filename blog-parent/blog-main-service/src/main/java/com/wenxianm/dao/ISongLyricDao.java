package com.wenxianm.dao;

import com.wenxianm.model.entity.SongLyric;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName ISongLyricDao
 * @Author cwx
 * @Date 2021/10/9 16:03
 **/
@Repository
public interface ISongLyricDao extends Mapper<SongLyric> {

}
