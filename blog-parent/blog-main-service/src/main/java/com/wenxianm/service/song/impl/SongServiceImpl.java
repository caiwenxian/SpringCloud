package com.wenxianm.service.song.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wenxianm.model.Page;
import com.wenxianm.model.entity.MqMessage;
import com.wenxianm.model.enums.*;
import com.wenxianm.model.mq.Mp3UrlMessage;
import com.wenxianm.model.param.ArtistParam;
import com.wenxianm.model.param.SongSearchParam;
import com.wenxianm.model.vo.Mp3UrlVO;
import com.wenxianm.model.vo.SongHotVO;
import com.wenxianm.model.vo.SongSearchVO;
import com.wenxianm.dao.ISongDao;
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
import com.wenxianm.service.RedisQueueService;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
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
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private RedisQueueService redisQueueService;

    @Override
    public PageData<SongDto> listSong(SongParam songParam) {
        Example example = new Example(Song.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(songParam.getName())) {
            criteria.andLike(PojoUtil.field(Song::getName), "%" + songParam.getName() + "%");
        }
        if (Objects.nonNull(songParam.getArtistId())) {
            criteria.andEqualTo(PojoUtil.field(Song::getArtistId), songParam.getArtistId());
        }
        example.setOrderByClause("num asc");
        int count = songDao.selectCountByExample(example);
        if (count == Constants.ZERO) {
            return PageData.emptyPage();
        }
        songParam.setTotalItem(count);
        long o = System.currentTimeMillis();
        log.info("start:{}", o);
        List<Song> songs = songDao.list(songParam);
        log.info("start:{}", System.currentTimeMillis() - o);
        List<SongDto> list = BeanUtil.fromList(songs, SongDto.class);
        List<Long> artistIds = list.stream().map(SongDto::getArtistId).distinct().collect(Collectors.toList());
        List<ArtistDto> artists = artistService.getByArtistIds(artistIds);
        Map<Long, ArtistDto> artistMap = artists.stream().collect(Collectors.toMap(ArtistDto::getArtistId, Function.identity()));
        list.forEach(v -> {
            ArtistDto artistDto = artistMap.get(v.getArtistId());
            if (Objects.nonNull(artistDto)) {
                v.setArtistName(artistDto.getName());
            }
        });
        return new PageData<>(list, songParam);
    }

    @Override
    public List<SongDto> list(SongParam songParam) {
        Example example = new Example(Song.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(songParam.getName())) {
            criteria.andLike(PojoUtil.field(Song::getName), "%" + songParam.getName() + "%");
        }
        if (Objects.nonNull(songParam.getArtistId())) {
            criteria.andEqualTo(PojoUtil.field(Song::getArtistId), songParam.getArtistId());
        }
        example.setOrderByClause("num asc");
        RowBounds rowBounds = new RowBounds(Constants.ZERO, Constants.FIVE_HUNDRED);
        List<Song> songs = songDao.selectByExampleAndRowBounds(example, rowBounds);
        if (CollectionUtils.isEmpty(songs)) {
            return Lists.newArrayList();
        }
        List<SongDto> list = BeanUtil.fromList(songs, SongDto.class);
        list.forEach(v -> {
            ArtistDto artist = artistService.getByArtistId(v.getArtistId());
            if (Objects.nonNull(artist)) {
                v.setArtistName(artist.getName());
            }
        });
        return list;
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
                if (song.getName().indexOf("DJ") > 0 || song.getName().indexOf("dj") > 0) {
                    log.info("过滤歌曲：{}", song);
                    return;
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
    public List<Song> reptileMp3Url(List<Long> songIds) {
        if (CollectionUtils.isEmpty(songIds)) {
            List<Song> songs = this.listSongNoMp3();
            if (CollectionUtils.isEmpty(songs)) {
                log.info("没有需要获取mp3的歌曲");
                return Lists.newArrayList();
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
        List<Song> list = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray songs = jsonObject.getJSONArray("data");
        songs.forEach(v -> {
            JSONObject json = (JSONObject)v;
            String map3Url = json.getString("url");
            Long songId = json.getLong("id");
            // 推送到mq
            Mp3UrlMessage mp3UrlMessage = new Mp3UrlMessage();
            mp3UrlMessage.setUrl(StringUtils.isEmpty(map3Url) ? "-1" : map3Url);
            mp3UrlMessage.setSongId(songId);
            MqProducerWithoutStream.MessageResponse messageResponse = mqProducer.output1().send(mp3UrlMessage,
                    MQTagEnum.BLOG_MAIN_UPDATE_MP3_URL,
                    DelayTimeLevelEnum.getDelayTimeLevel(Constants.ONE));
            if (messageResponse.getSuccess()) {
                MqMessage message = MqMessage.initRocketMq(MqMessageTypeEnum.REPTILE_TOP_SONG.getCode(),
                        MqMessageStatusEnum.DOING.getCode(),
                        JsonUtil.objToStr(mp3UrlMessage),
                        messageResponse.getId()
                );

                mqMessageService.add2Redis(message);
            }
            Song songEntity = new Song();
            songEntity.setSongId(songId);
            songEntity.setMp3Url(StringUtils.isEmpty(map3Url) ? "-1" : map3Url);
            list.add(songEntity);
        });
        log.info("reptile mp3url完成");
        return list;
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
                // 手动提交事务，降低事务范围
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
                topUrls = topElements.stream().map(v -> v.attr("href"))
                        .filter(v -> {
                            String id = v.split("=")[1];
                            return TopListTypeEnum.isMainTop(id);
                        })
                        .distinct().collect(Collectors.toList());
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
                Artist artist = new Artist();
                artist.setArtistId(artistId);
                artist.setReptileHotSong(Constants.NEGATIVE_ONE);
                artistService.updateOne(artist);
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
            JSONObject artistObj = jsonObject.getJSONObject("artist");
            String phone = artistObj.getString("picUrl");
            Artist artist = new Artist();
            artist.setArtistId(artistId);
            artist.setPhoto(phone);
            artist.setReptileHotSong(Constants.ONE);
            artistService.updateOne(artist);
        } catch (Exception e) {
            log.error("reptile歌手的热门歌曲出错", e);
            throw new BaseException(ServerCode.ERROR, "reptile歌手的热门歌曲出错");
        }
    }

    @Override
    public SongSearchVO search(SongSearchParam searchParam) {
        Integer count = 0;
        try {
            List<ArtistDto> artists = artistService.list(new ArtistParam(searchParam.getSearchKey()));
            if (!CollectionUtils.isEmpty(artists)) {
                searchParam.setArtistId(artists.get(0).getArtistId());
            }
            count = songDao.countSearch(searchParam);
            if (count.equals(Constants.ZERO)) {
                return new SongSearchVO(PageData.emptyPage(searchParam), artists);
            }
            searchParam.setTotalItem(count);
            List<SongDto> list = songDao.listSearch(searchParam);
            List<Long> artistIds = list.stream().map(SongDto::getArtistId).distinct().collect(Collectors.toList());
            List<ArtistDto> artistList = artistService.getByArtistIds(artistIds);
            Map<Long, ArtistDto> artistMap = artistList.stream().collect(Collectors.toMap(ArtistDto::getArtistId, Function.identity()));
            list.forEach(v -> {
                ArtistDto artistDto = artistMap.get(v.getArtistId());
                if (Objects.nonNull(artistDto)) {
                    v.setArtistName(artistDto.getName());
                }
            });
            PageData pageData = new PageData(list, new Page(searchParam));
            return new SongSearchVO(pageData, artists);
        } catch (Exception e) {
            log.error("搜索出错", e);
            throw new BaseException(ServerCode.ERROR, "搜索出错");
        } finally {
            if (count == 0) {
                // 将搜索关键词push到队列
                redisQueueService.lPush(Constants.REPTILE_SONG_KEY, searchParam.getSearchKey());
            }
        }
    }

    @Override
    public List<SongHotVO> hotSong() {
        SongParam songParam = new SongParam();
        songParam.setPageIndex(1);
        songParam.setPageSize(20);
        PageData<SongDto> pageData = this.listSong(songParam);
        List<SongDto> list = pageData.getList();
        List<SongHotVO> songHotVOS = BeanUtil.fromList(list, SongHotVO.class);
        songHotVOS.forEach(v -> {
            v.setPlayTimes(IDUtil.randomInRange(1, 100));
        });
        return songHotVOS;
    }

    @Override
    public List<Mp3UrlVO> getMp3(List<Long> songIds) {
        List<Mp3UrlVO> list = new ArrayList<>();
        List<Song> songs = reptileMp3Url(songIds);
        for (Song song : songs) {
            if (StringUtils.isEmpty(song.getMp3Url()) || song.getMp3Url().equals(Constants.STR_NEGATIVE_ONE)) {
                continue;
            }
            String url = String.format(Constants.WANG_YI_SONG_MP3_URL, song.getSongId());
            list.add(new Mp3UrlVO(song.getSongId(), url));
        }
        /*for (Long songId : songIds) {
            String url = String.format(Constants.WANG_YI_SONG_MP3_URL, songId);
            SongDto song = this.getBySongId(songId);
            if (StringUtils.isEmpty(song.getMp3Url()) || song.getMp3Url().equals(Constants.STR_NEGATIVE_ONE)) {
                continue;
            }
            list.add(new Mp3UrlVO(songId, url));
        }*/
        return list;
    }

    @Override
    public void consumerWaitReptile() {
        redisQueueService.bRPopLPush(Constants.REPTILE_SONG_KEY, (value) -> {
            songService.reptileSong(value);
        }, Constants.TEN_THOUSAND_L);
    }
}
