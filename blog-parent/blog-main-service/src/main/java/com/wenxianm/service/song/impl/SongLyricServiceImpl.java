package com.wenxianm.service.song.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wenxianm.dao.ISongLyricDao;
import com.wenxianm.exception.BaseException;
import com.wenxianm.model.Constants;
import com.wenxianm.model.ServerCode;
import com.wenxianm.model.entity.SongLyric;
import com.wenxianm.model.vo.SongLyricVO;
import com.wenxianm.service.song.ISongLyricService;
import com.wenxianm.utils.BeanUtil;
import com.wenxianm.utils.HttpClientHelper;
import com.wenxianm.utils.IDUtil;
import com.wenxianm.utils.PojoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 歌词业务
 * @ClassName SongLyricServiceImpl
 * @Author cwx
 * @Date 2021/12/7 15:50
 **/
@Service
@Slf4j
public class SongLyricServiceImpl implements ISongLyricService {

    @Autowired
    private ISongLyricDao songLyricDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOne(SongLyric songLyric) {
        songLyric.setId(IDUtil.random4s());
        songLyric.setCreateTime(new Date());
        songLyric.setModifyTime(new Date());
        songLyricDao.insertSelective(songLyric);
    }

    @Override
    public void saveBatch(List<SongLyric> songLyrics) {
        for (SongLyric songLyric : songLyrics) {
            this.saveOne(songLyric);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(List<SongLyric> songLyrics) {
        for (SongLyric songLyric : songLyrics) {
            if (Objects.isNull(songLyric.getId())) {
                continue;
            }
            songLyric.setModifyTime(new Date());
            Example example = new Example(SongLyric.class);
            example.createCriteria().andEqualTo(PojoUtil.field(SongLyric::getId), songLyric.getId());
            songLyricDao.updateByExampleSelective(songLyric, example);
        }

    }

    @Override
    public SongLyric getOne(String id) {
        Example example = new Example(SongLyric.class);
        example.createCriteria().andEqualTo(PojoUtil.field(SongLyric::getId), id);
        List<SongLyric> songLyrics = songLyricDao.selectByExample(example);
        if (CollectionUtils.isEmpty(songLyrics)) {
            return null;
        }
        return songLyrics.get(0);
    }

    @Override
    public List<SongLyric> list(List<String> ids) {
        Example example = new Example(SongLyric.class);
        example.createCriteria().andIn(PojoUtil.field(SongLyric::getId), ids);
        List<SongLyric> songLyrics = songLyricDao.selectByExample(example);
        if (CollectionUtils.isEmpty(songLyrics)) {
            return Lists.newArrayList();
        }
        return songLyrics;
    }

    @Override
    public List<SongLyricVO> listBySongIds(List<Long> songIds) {
        if (CollectionUtils.isEmpty(songIds)) {
            return Lists.newArrayList();
        }
        Example example = new Example(SongLyric.class);
        example.createCriteria().andIn(PojoUtil.field(SongLyric::getSongId), songIds);
        List<SongLyric> songLyrics = songLyricDao.selectByExample(example);
        if (CollectionUtils.isEmpty(songLyrics)) {
            return Lists.newArrayList();
        }
        return BeanUtil.fromList(songLyrics, SongLyricVO.class);
    }

    @Override
    public void reptileLyric() {
        Example example = new Example(SongLyric.class);
        example.createCriteria().andEqualTo(PojoUtil.field(SongLyric::getHasLyric), Constants.ZERO);
        RowBounds rowBounds = new RowBounds(Constants.ZERO, Constants.TEEN);
        List<SongLyric> noLyrics = songLyricDao.selectByExampleAndRowBounds(example, rowBounds);
        if (CollectionUtils.isEmpty(noLyrics)) {
            return;
        }
        StringBuffer url = new StringBuffer();
        url.append(Constants.WANG_YI_API);
        url.append(Constants.WANG_YI_LYRIC);
        List<SongLyric> list = new ArrayList<>();
        for (SongLyric noLyric : noLyrics) {
            if (noLyric.getSongId().equals(Constants.ZERO_L)) {
                continue;
            }
            Map<String, Object> params = new HashMap<>(Constants.TEEN);
            params.put("id", noLyric.getSongId());
            String result;
            try {
                result = HttpClientHelper.sendGet(url.toString(), params, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("请求网易接口出错: ", e);
                throw new BaseException(ServerCode.ERROR, "请求网易接口出错");
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!Constants.HttpStatus.TWO_HUNDRED.equals(jsonObject.getInteger("code"))) {
                continue;
            }
            String lyric = jsonObject.getString("lyric");
            Integer version = jsonObject.getInteger("lyricVersion");
            SongLyric songLyric = new SongLyric();
            songLyric.setId(noLyric.getId());
            songLyric.setLyric(lyric);
            songLyric.setVersion(version);
            songLyric.setHasLyric(StringUtils.isEmpty(lyric) ? Constants.NEGATIVE_ONE : Constants.ONE);
            if (!StringUtils.isEmpty(lyric) && lyric.length() > 15000) {
                songLyric.setLyric(null);
                songLyric.setHasLyric(Constants.NEGATIVE_ONE);
            }
            list.add(songLyric);
        }
        if (!CollectionUtils.isEmpty(list)) {
            this.updateBatch(list);
        }
    }
}
