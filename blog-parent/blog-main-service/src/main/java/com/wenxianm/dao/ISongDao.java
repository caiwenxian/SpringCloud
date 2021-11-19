package com.wenxianm.dao;

import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.entity.Song;
import com.wenxianm.model.param.SongParam;
import com.wenxianm.model.param.SongSearchParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ClassName ISongDao
 * @Author cwx
 * @Date 2021/10/9 16:03
 **/
@Repository
public interface ISongDao extends Mapper<Song> {

    /**
     * 搜索
     * @param songSearchParam
     * @author caiwx
     * @date 2021/11/18 - 15:20
     * @return Integer
     **/
    Integer countSearch(@Param("songSearchParam") SongSearchParam songSearchParam);

    /**
     * 搜索
     * @param songSearchParam
     * @author caiwx
     * @date 2021/11/18 - 15:20
     * @return List<SongDto>
     **/
    List<SongDto> listSearch(@Param("songSearchParam") SongSearchParam songSearchParam);

    /**
     * 分页获取
     * @param songParam
     * @author caiwx
     * @date 2021/11/18 - 16:42
     * @return List<SongDto>
     **/
    List<Song> list(@Param("songParam") SongParam songParam);
}
