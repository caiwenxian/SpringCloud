package com.mue.demo.dao.sharding;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mue.demo.model.sharding.OrderEntity;
import com.mue.demo.model.sharding.Song;
import org.springframework.stereotype.Component;

@Component
public interface SongDao extends BaseMapper<Song> {
    int deleteByPrimaryKey(String id);

    int insert(Song record);

    int insertSelective(Song record);

    Song selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Song record);

    int updateByPrimaryKey(Song record);
}