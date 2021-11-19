package com.wenxianm.controller;

import com.google.common.collect.Lists;
import com.wenxianm.annotation.ApiMethod;
import com.wenxianm.api.SongApi;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.entity.MqMessage;
import com.wenxianm.model.enums.MQTagEnum;
import com.wenxianm.model.enums.MqMessageStatusEnum;
import com.wenxianm.model.enums.MqMessageTypeEnum;
import com.wenxianm.model.mq.TopSongMessage;
import com.wenxianm.model.param.SongParam;
import com.wenxianm.model.param.SongSearchParam;
import com.wenxianm.model.vo.Mp3UrlVO;
import com.wenxianm.model.vo.SongHotVO;
import com.wenxianm.model.vo.SongSearchVO;
import com.wenxianm.mq.MqProducer;
import com.wenxianm.mq.MqProducerWithoutStream;
import com.wenxianm.service.mq.IMqMessageService;
import com.wenxianm.service.song.ISongService;
import com.wenxianm.utils.IDUtil;
import com.wenxianm.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SongController
 * @Author cwx
 * @Date 2021/9/24 18:19
 **/
@RestController
@RequestMapping("/song")
@Slf4j
public class SongController implements SongApi{

    @Autowired
    ISongService songService;
    @Autowired
    MqProducerWithoutStream mqProducer;
    @Autowired
    IMqMessageService mqMessageService;

    @ApiMethod
    @Override
    public PageData<SongDto> listSong(SongParam songParam) {
        return songService.listSong(songParam);
    }

    @Override
    public void reptileSong(String name) {
        songService.reptileSong(name);
    }

    @Override
    public void reptileMp3Url(@RequestBody List<Long> songIds) {
        songService.reptileMp3Url(songIds);
    }

    @Override
    public void reptileTopList(@RequestBody List<String> urls) {
        songService.reptileTopList(urls);
    }

    @Override
    public void reptileArtistHotSongs(Long artistId) {
        songService.reptileArtistHotSongs(artistId);
    }

    /**
     * 搜索
     * @param songSearchParam
     * @author caiwx
     * @date 2021/11/3 - 16:37
     * @return SongSearchVO
     **/
    @GetMapping("/search")
    public SongSearchVO search(SongSearchParam songSearchParam) {
        if (StringUtils.isBlank(songSearchParam.getSearchKey())) {
            return new SongSearchVO();
        }
        songSearchParam.setPageSize(30);
        songSearchParam.setSearchKey(songSearchParam.getSearchKey().trim());
        return songService.search(songSearchParam);
    }

    /**
     * 获取热门歌曲
     * @author caiwx
     * @date 2021/11/4 - 17:24
     * @return List<SongHotVO>
     **/
    @GetMapping("/hot/songs")
    public List<SongHotVO> hotSong() {
        return songService.hotSong();
    }

    /**
     * 获取mp3播放外链
     * @param songSearchParam#songIds 歌曲id
     * @author caiwx
     * @date 2021/11/6 - 9:57
     * @return String
     **/
    @PostMapping("/get/mp3")
    public List<Mp3UrlVO> getMp3(@RequestBody SongSearchParam songSearchParam) {
        return songService.getMp3(songSearchParam.getSongIds());
    }

    /**
     * 根据歌手获取歌曲
     * @param artistId 歌手id
     * @author caiwx
     * @date 2021/11/9 - 11:44
     * @return List<SongDto>
     **/
    @GetMapping("/list/artist/song")
    public List<SongDto> listSongByArtist(Long artistId) {
        SongParam songParam = new SongParam();
        songParam.setArtistId(artistId);
        return songService.list(songParam);
    }

    @GetMapping("testMq")
    public void testMq() {
        TopSongMessage topSongMessage = new TopSongMessage();
        topSongMessage.setId(IDUtil.random());
        topSongMessage.setUrl("/discover/toplist?id=5453912201");
        MqProducerWithoutStream.MessageResponse messageResponse = mqProducer.output1().send(topSongMessage, MQTagEnum.BLOG_TEST);
        if (messageResponse.getSuccess()) {
            MqMessage message = MqMessage.initRocketMq(MqMessageTypeEnum.REPTILE_TOP_SONG.getCode(),
                    MqMessageStatusEnum.DOING.getCode(),
                    JsonUtil.objToStr(topSongMessage),
                    messageResponse.getId()
            );
            mqMessageService.addOne(message);
        }
    }
}
