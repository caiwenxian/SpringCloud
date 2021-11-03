package com.wenxianm.service.song.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wenxianm.model.entity.MqMessage;
import com.wenxianm.model.enums.*;
import com.wenxianm.model.param.ArtistParam;
import com.wenxianm.model.vo.SongSearchVO;
import com.wenxianm.mq.MqProducer;
import com.wenxianm.dao.song.ISongDao;
import com.wenxianm.exception.BaseException;
import com.wenxianm.model.Constants;
import com.wenxianm.model.PageData;
import com.wenxianm.model.ServerCode;
import com.wenxianm.model.dto.ArtistDto;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.entity.Artist;
import com.wenxianm.model.entity.Song;
import com.wenxianm.model.mq.TopSongMessage;
import com.wenxianm.model.param.SongParam;
import com.wenxianm.mq.MqProducerWithoutStream;
import com.wenxianm.service.mq.IMqMessageService;
import com.wenxianm.service.song.IArtistService;
import com.wenxianm.service.song.ISongService;
import com.wenxianm.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.util.*;
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
    private ISongDao songDao;
    @Autowired
    private IArtistService artistService;
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Autowired
    private ISongService songService;
    @Autowired
    private MqProducerWithoutStream mqProducer;
    @Autowired
    private IMqMessageService mqMessageService;

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
    public List<SongDto> list(SongParam songParam) {
        Example example = new Example(Song.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(PojoUtil.field(Song::getName), "%" + songParam.getName() + "%");
        List<Song> songs = songDao.selectByExample(example);
        if (CollectionUtils.isEmpty(songs)) {
            return Lists.newArrayList();
        }
        return BeanUtil.fromList(songs, SongDto.class);
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
            synchronized (this) {
                if (Objects.isNull(song.getSongId())) {
                    log.error("歌曲id不可为空");
                    throw new BaseException(ServerCode.WRONG_PARAM, "歌曲id不可为空");
                }
                if (this.getBySongId(song.getSongId()) != null) {
                    return;
                }
                song.setId(IDUtil.random().toString());
                song.setCreateTime(new Date());
                song.setModifyTime(new Date());
                songDao.insertSelective(song);
            }
        } catch (Exception e) {
            log.error("新增歌曲出错: ", e);
            throw new BaseException(ServerCode.ERROR, "新增歌曲出错");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSong(Song song) {
        try {
            synchronized (this) {
                if (Objects.isNull(song.getSongId())) {
                    log.warn("歌曲id不能为空");
                    return;
                }
                song.setModifyTime(new Date());
                Example example = new Example(Song.class);
                example.createCriteria().andEqualTo(PojoUtil.field(Song::getSongId), song.getSongId());
                songDao.updateByExampleSelective(song, example);
            }
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
    @Transactional(rollbackFor = Exception.class)
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
                Song songEntity = new Song();
                songEntity.setSongId(songId);
                songEntity.setName(song.getString("name"));
                songEntity.setNum(num);
                songEntity.setOrigin(SongOriginEnum.WANG_YI.getCode());
                Long artistId = ((JSONObject) song.getJSONArray("artists").get(0)).getLong("id");
                songEntity.setArtistId(artistId);
                songService.addSong(songEntity);
                String artistName = ((JSONObject) song.getJSONArray("artists").get(0)).getString("name");
                Artist artist = new Artist(artistId, artistName, SongOriginEnum.WANG_YI.getCode());
                artistService.addOne(artist);
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
            log.info("top_url: ", topUrl);
            // 获取第一个排行榜的歌曲
            Document document = Jsoup.connect(Constants.WANG_YI_API + topUrl).get();
            String json = document.select("textarea[id=song-list-pre-data]").text();
            JSONArray jsonArray = JSON.parseArray(json);
            jsonArray.forEach(v -> {
                // 手动提交事务，减低事务范围
                TransactionStatus transactionStatus = null;
                try {
                    transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                    JSONObject jsonObject = (JSONObject) v;
                    Long songId = jsonObject.getLong("id");
                    String name = jsonObject.getString("name");
                    Long artistId = ((JSONObject) jsonObject.getJSONArray("artists").get(0)).getLong("id");
                    Song song = new Song(songId, name, artistId, SongOriginEnum.WANG_YI.getCode());
                    song.setNum(Constants.ONE);
                    songService.addSong(song);
                    String artistName = ((JSONObject) jsonObject.getJSONArray("artists").get(0)).getString("name");
                    Artist artist = new Artist(artistId, artistName, SongOriginEnum.WANG_YI.getCode());
                    artistService.addOne(artist);
                    dataSourceTransactionManager.commit(transactionStatus);
                } catch(Exception e) {
                    log.error("sql执行异常，触发回滚");
                    dataSourceTransactionManager.rollback(transactionStatus);
                }
            });
            // 获取其他排行榜
            if (CollectionUtils.isEmpty(topUrls)) {
                Elements topElements = document.select("a[href^=/discover]");
                topUrls = topElements.stream().map(v -> v.attr("href")).distinct().collect(Collectors.toList());
                topUrls.remove(0);
                int index = 1;
                for (String url : topUrls) {
                    // 推送到mq
                    TopSongMessage topSongMessage = new TopSongMessage();
                    topSongMessage.setUrl(url);
                    DelayTimeLevelEnum delayTimeLevel = DelayTimeLevelEnum.getDelayTimeLevel(index);
                    MqProducerWithoutStream.MessageResponse messageResponse = mqProducer.output1().send(topSongMessage, MQTagEnum.BLOG_MAIN_REPTILE_TOP_SONG, delayTimeLevel);
                    index = index > 17 ? index : index + 1;
                    if (messageResponse.getSuccess()) {
                        MqMessage message = MqMessage.initRocketMq(MqMessageTypeEnum.REPTILE_TOP_SONG.getCode(),
                                MqMessageStatusEnum.DOING.getCode(),
                                JsonUtil.objToStr(topSongMessage),
                                messageResponse.getId()
                                );
                        mqMessageService.addOne(message);
                    }
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
    public void reptileArtistHotSongs(Long artistId) {
        try {
            List<Long> artists = Lists.newArrayList();
            if (Objects.isNull(artistId) || artistId.equals(Constants.ZERO_L)) {
                List<ArtistDto> artistDtos = artistService.listNoHotSong();
                List<Long> ids = artistDtos.stream().map(v -> v.getArtistId()).distinct().collect(Collectors.toList());
                artists.addAll(ids);
            } else {
                artists.add(artistId);
            }
            if (CollectionUtils.isEmpty(artists)) {
                log.info("没有需要reptile的歌手");
                return;
            }
            artists.forEach(v -> {
                // 通过songService调用才能开启事务
                songService.reptileHotSongs(v);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            log.error("reptile歌手的热门歌曲出错", e);
            throw new BaseException(ServerCode.ERROR, "reptile歌手的热门歌曲出错");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reptileHotSongs(Long artistId) {
        try {
            StringBuffer url = new StringBuffer();
            url.append(Constants.WANG_YI_API);
            url.append(Constants.WANG_YI_HOT_SONG);
            url.append(artistId);
            Map<String,Object> paramMap = new HashMap(2);
            paramMap.put("limit", 50);
            paramMap.put("offset", 0);
            String result = HttpClientHelper.sendGet(url.toString(), paramMap, "UTF8");
            if (StringUtils.isEmpty(result)) {
                log.info("reptile结果为空");
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            if (!Objects.equals(code, Constants.TWO_HUNDRED)) {
                log.info("reptile返回code不是200");
                return;
            }
            JSONArray songs = jsonObject.getJSONArray("hotSongs");
            //保存歌曲
            int num = 1;
            for (Object object : songs) {
                JSONObject song = (JSONObject) object;
                Long songId = song.getLong("id");
                String name = song.getString("name");
                Song entity = new Song(songId, name, artistId, SongOriginEnum.WANG_YI.getCode(), num);
                this.addSong(entity);
                num++;
            }
            Artist artist = new Artist();
            artist.setArtistId(artistId);
            artist.setReptileHotSong(Constants.ONE);
            artistService.updateOne(artist);
        } catch (Exception e) {
            log.error("reptile歌手的热门歌曲出错", e);
            throw new BaseException(ServerCode.ERROR, "reptile歌手的热门歌曲出错");
        }
    }

    @Override
    public SongSearchVO search(String name) {
        List<SongDto> songs = this.list(new SongParam(name));
        songs.forEach(v -> {
            ArtistDto artist = artistService.getByArtistId(v.getArtistId());
            if (Objects.nonNull(artist)) {
                v.setArtistName(artist.getName());
            }
        });
        List<ArtistDto> artists = artistService.list(new ArtistParam(name));
        return new SongSearchVO(songs, artists);
    }
}
