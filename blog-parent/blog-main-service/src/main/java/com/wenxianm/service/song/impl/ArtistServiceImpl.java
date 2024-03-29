package com.wenxianm.service.song.impl;

import com.google.common.collect.Lists;
import com.wenxianm.dao.IArtistDao;
import com.wenxianm.exception.BaseException;
import com.wenxianm.model.Constants;
import com.wenxianm.model.PageData;
import com.wenxianm.model.ServerCode;
import com.wenxianm.model.dto.ArtistDto;
import com.wenxianm.model.entity.Artist;
import com.wenxianm.model.param.ArtistParam;
import com.wenxianm.service.song.IArtistService;
import com.wenxianm.utils.BeanUtil;
import com.wenxianm.utils.IDUtil;
import com.wenxianm.utils.PojoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName ArtistServiceImpl
 * @Author cwx
 * @Date 2021/10/20 10:38
 **/
@Service
@Slf4j
public class ArtistServiceImpl implements IArtistService {

    @Autowired
    private IArtistDao artistDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOne(Artist artist) {
        try {
            if (Objects.isNull(artist.getArtistId())) {
                log.warn("歌手id不可为空");
                throw new BaseException(ServerCode.WRONG_PARAM, "歌手id不可为空");
            }
            if (this.getByArtistId(artist.getArtistId()) != null) {
                log.info("歌手已存在");
                return;
            }
            artist.setId(IDUtil.random4s());
            artist.setCreateTime(new Date());
            artist.setModifyTime(new Date());
            artistDao.insertSelective(artist);
        } catch (BaseException e) {
            log.error("新增歌手出错", e);
            throw new BaseException(e.getServerCode(), e.getDesc());
        } catch (Exception e) {
            log.error("新增歌手出错", e);
            throw new BaseException(ServerCode.ERROR, "新增歌手出错");
        }

    }

    @Override
    public void updateOne(Artist artist) {
        if (Objects.isNull(artist.getArtistId())) {
            log.info("歌手id不可为空");
            return;
        }
        artist.setModifyTime(new Date());
        Example example = new Example(Artist.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(PojoUtil.field(Artist::getArtistId), artist.getArtistId());
        artistDao.updateByExampleSelective(artist, example);
    }

    @Override
    public ArtistDto getOne() {
        return null;
    }

    @Override
    public ArtistDto getByArtistId(Long artistId) {
        Example example = new Example(Artist.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(PojoUtil.field(Artist::getArtistId), artistId);
        List<Artist> artists = artistDao.selectByExample(example);
        if (CollectionUtils.isEmpty(artists)) {
            return null;
        }
        return BeanUtil.from(artists.get(0), ArtistDto.class);
    }

    @Override
    public List<ArtistDto> getByArtistIds(List<Long> artistIds) {
        Example example = new Example(Artist.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(PojoUtil.field(Artist::getArtistId), artistIds);
        List<Artist> artists = artistDao.selectByExample(example);
        if (CollectionUtils.isEmpty(artists)) {
            return Lists.newArrayList();
        }
        return BeanUtil.fromList(artists, ArtistDto.class);
    }

    @Override
    public List<ArtistDto> list(ArtistParam artistParam) {
        Example example = new Example(Artist.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(PojoUtil.field(Artist::getName), "%" + artistParam.getName() + "%");
        List<Artist> artists = artistDao.selectByExample(example);
        if (CollectionUtils.isEmpty(artists)) {
           return Lists.newArrayList();
        }
        return BeanUtil.fromList(artists, ArtistDto.class);
    }

    @Override
    public PageData<ArtistDto> listPage(ArtistParam artistParam) {
        return null;
    }

    @Override
    public List<ArtistDto> listNoHotSong() {
        Example example = new Example(Artist.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(PojoUtil.field(Artist::getReptileHotSong), Constants.ZERO);
        example.setOrderByClause("name desc");
        RowBounds rowBounds = new RowBounds(Constants.ZERO, Constants.FIVE);
        List<Artist> artists = artistDao.selectByExampleAndRowBounds(example, rowBounds);
        if (CollectionUtils.isEmpty(artists)) {
            return Lists.newArrayList();
        }
        return BeanUtil.fromList(artists, ArtistDto.class);
    }
}
