package com.wenxianm.service.song.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wenxianm.dao.song.ISongDao;
import com.wenxianm.exception.BaseException;
import com.wenxianm.model.Constants;
import com.wenxianm.model.Page;
import com.wenxianm.model.PageData;
import com.wenxianm.model.ServerCode;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.entity.Song;
import com.wenxianm.model.enums.SongOriginEnum;
import com.wenxianm.model.param.SongParam;
import com.wenxianm.service.song.ISongService;
import com.wenxianm.utils.BeanUtil;
import com.wenxianm.utils.HttpClientHelper;
import com.wenxianm.utils.IDUtil;
import com.wenxianm.utils.PojoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName SongServiceImpl
 * @Author cwx
 * @Date 2021/10/9 16:28
 **/
@Service
@Slf4j
public class SongServiceImpl implements ISongService {

    @Autowired
    ISongDao songDao;

    @Override
    public PageData<SongDto> listSong(SongParam songParam) {
        Example example = new Example(Song.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(PojoUtil.field(Song::getName),songParam.getName());
        int count = songDao.selectCountByExample(example);
        if (count == Constants.ZERO) {
            return PageData.emptyPage();
        }
        songParam.setTotalItem(count);
        RowBounds rowBounds = new RowBounds(songParam.getOffset(), songParam.getLimit());
        List<Song> songs = songDao.selectByExampleAndRowBounds(example, rowBounds);
        List<SongDto> list = BeanUtil.fromList(songs, SongDto.class);
        return new PageData<>(list, songParam);
    }

    @Override
    public List<Song> listSongNoMp3() {
        Example example = new Example(Song.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(PojoUtil.field(Song::getMp3Url), Constants.ZERO.toString());
        RowBounds rowBounds = new RowBounds(Constants.ZERO, Constants.THIRTY);
        return songDao.selectByExampleAndRowBounds(example, rowBounds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSong(Song song) {
        try {
            song.setId(IDUtil.random().toString());
            song.setCreateTime(new Date());
            song.setModifyTime(new Date());
            songDao.insertSelective(song);
        } catch (Exception e) {
            log.error("新增歌曲出错: ", e);
            throw new BaseException(ServerCode.ERROR, "新增歌曲出错");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSong(Song song) {
        try {
            if (Objects.isNull(song.getSongId())) {
                log.warn("歌曲id不能为空");
                return;
            }
            song.setModifyTime(new Date());
            Example example = new Example(Song.class);
            example.createCriteria().andEqualTo(PojoUtil.field(Song::getSongId), song.getSongId());
            songDao.updateByExampleSelective(song, example);
        } catch (Exception e) {
            log.error("更新歌曲出错: ", e);
            throw new BaseException(ServerCode.ERROR, "更新歌曲出错");
        }
    }

    @Override
    public SongDto getBySongId(Long songId) {
        Example example = new Example(Song.class);
        example.createCriteria().andEqualTo(PojoUtil.field(Song::getSongId), songId);
        List<Song> songs = songDao.selectByExample(example);
        if (CollectionUtils.isEmpty(songs)) {
            return null;
        }
        return BeanUtil.from(songs.get(0), SongDto.class);
    }

    @Override
    public void reptileSong(String name) {
        try {
            StringBuffer url = new StringBuffer();
            url.append(Constants.WANG_YI_API);
            url.append(Constants.WANG_YI_SEARCH);
            Map<String, Object> params = new HashMap<>(5);
            params.put("limit", 10);
            params.put("type", 1);
            params.put("limit", 10);
            params.put("offset", 0);
            params.put("s", name);

            String result = null;
            try {
                result = HttpClientHelper.sendGet(url.toString(), params, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("请求网易接口出错: ", e);
                throw new BaseException(ServerCode.ERROR, "请求网易接口出错");
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject object = (JSONObject) jsonObject.get("result");
            JSONArray songs = object.getJSONArray("songs");
            if (Objects.isNull(songs) || songs.isEmpty()) {
                log.info("爬取结果为空");
                return;
            }
            int num = 1;
            for (Object ob : songs) {
                JSONObject song = (JSONObject) ob;
                Long songId = song.getLong("id");
                if (this.getBySongId(songId) == null) {
                    Song songEntity = new Song();
                    songEntity.setSongId(songId);
                    songEntity.setName(song.getString("name"));
                    songEntity.setNum(num);
                    songEntity.setOrigin(SongOriginEnum.WANG_YI.getName());
                    Long artistId = ((JSONObject) song.getJSONArray("artists").get(0)).getLong("id");
                    songEntity.setArtistId(artistId);
                    this.addSong(songEntity);
                }
                num++;
            }
            log.info("爬取歌曲完成");
        } catch (Exception e) {
            log.error("根据名称reptile歌曲出错", e);
            throw new BaseException(ServerCode.ERROR, "根据名称reptile歌曲出错");
        }

    }

    @Override
    public void reptileMp3Url(List<Long> songIds) {
        if (CollectionUtils.isEmpty(songIds)) {
            List<Song> songs = this.listSongNoMp3();
            if (CollectionUtils.isEmpty(songs)) {
                log.info("没有需要获取mp3的歌曲");
                return;
            }
            List<Long> ids = songs.stream().map(v -> v.getSongId()).distinct().collect(Collectors.toList());
            songIds.addAll(ids);
        }
        StringBuffer url = new StringBuffer();
        url.append(Constants.WANG_YI_API);
        url.append(Constants.WANG_YI_MP3_URL);
        Map<String, Object> params = new HashMap<>(5);
        params.put("br", 3200000);
        params.put("ids", songIds);
        String result;
        try {
            result = HttpClientHelper.sendGet(url.toString(), params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("请求网易接口出错: ", e);
            throw new BaseException(ServerCode.ERROR, "请求网易接口出错");
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray songs = jsonObject.getJSONArray("data");
        songs.forEach(v -> {
            JSONObject json = (JSONObject)v;
            String map3Url = json.getString("url");
            Long songId = json.getLong("id");
            //添加歌曲mp3url
            Song songEntity = new Song();
            songEntity.setSongId(songId);
            songEntity.setMp3Url(StringUtils.isEmpty(map3Url) ? "-1" : map3Url);
            this.updateSong(songEntity);
        });
        log.info("reptile mp3url完成");
    }

    @Override
    public void reptileTopList(List<String> topUrls) {
        try {
            String topUrl = "/discover/toplist";
            if (!CollectionUtils.isEmpty(topUrls)) {
                topUrl = topUrls.get(0);
            }
            log.info(topUrl);
            // 获取第一个排行榜的歌曲
            Document document = Jsoup.connect(Constants.WANG_YI_API + topUrl).get();
            String json = document.select("textarea[id=song-list-pre-data]").text();
            JSONArray jsonArray = JSON.parseArray(json);
            jsonArray.forEach(v -> {
                JSONObject jsonObject = (JSONObject) v;
                Long songId = jsonObject.getLong("id");
                String name = jsonObject.getString("name");
                Long artistId = ((JSONObject) jsonObject.getJSONArray("artists").get(0)).getLong("id");
                Song song = new Song(songId, name, artistId);
                if (this.getBySongId(songId) == null) {
                    this.addSong(song);
                }

            });
            // 获取其他排行榜
            if (CollectionUtils.isEmpty(topUrls)) {
                Elements topElements = document.select("a[href^=/discover]");
                topUrls = topElements.stream().map(v -> v.attr("href")).distinct().collect(Collectors.toList());
                topUrls.remove(0);
                while (!CollectionUtils.isEmpty(topUrls)) {
                    Thread.sleep(3000);
                    this.reptileTopList(topUrls);
                }
                return;
            }
            topUrls.remove(0);
        } catch (Exception e) {
            log.error("reptile歌曲排行榜出错", e);
            throw new BaseException(ServerCode.ERROR, "reptile歌曲排行榜出错");
        }
    }

    @Override
    public void reptileArtistHotSongs(Long artist) {
        try {
            if (Objects.isNull(artist)) {

            }
        } catch (Exception e) {
            log.error("reptile歌手的热门歌曲出错", e);
            throw new BaseException(ServerCode.ERROR, "reptile歌手的热门歌曲出错");
        }
    }
}
