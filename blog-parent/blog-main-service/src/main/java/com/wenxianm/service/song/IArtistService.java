package com.wenxianm.service.song;

import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.ArtistDto;
import com.wenxianm.model.entity.Artist;
import com.wenxianm.model.param.ArtistParam;

import java.util.List;

/**
 * 歌手
 * @ClassName IArtistService
 * @Author cwx
 * @Date 2021/10/20 10:38
 **/
public interface IArtistService {

    /**
     * 新增歌手
     * @param artist
     * @author caiwx
     * @date 2021/10/20 - 10:42
     **/
    void addOne(Artist artist);

    /**
     * 更新歌手
     * @param artist
     * @author caiwx
     * @date 2021/10/20 - 10:43
     **/
    void updateOne(Artist artist);

    /**
     * 获取单个
     * @author caiwx
     * @date 2021/10/21 - 10:33
     * @return ArtistDto
     **/
    ArtistDto getOne();

    /**
     * 根据artistId获取
     * @param artistId 歌手id
     * @author caiwx
     * @date 2021/10/21 - 10:34
     * @return ArtistDto
     **/
    ArtistDto getByArtistId(Long artistId);

    /**
     * 获取列表
     * @param artistParam 查询参数
     * @author caiwx
     * @date 2021/10/20 - 10:47
     * @return List<ArtistDto>
     **/
    List<ArtistDto> list(ArtistParam artistParam);

    /**
     * 获取分页列表
     * @param artistParam 查询参数
     * @author caiwx
     * @date 2021/10/20 - 10:48
     * @return PageData<ArtistDto>
     **/
    PageData<ArtistDto> listPage(ArtistParam artistParam);

    /**
     * 获取5个没有reptile热门歌曲的歌手
     * @author caiwx
     * @date 2021/10/20 - 11:43
     * @return List<ArtistDto>
     **/
    List<ArtistDto> listNoHotSong();
}
