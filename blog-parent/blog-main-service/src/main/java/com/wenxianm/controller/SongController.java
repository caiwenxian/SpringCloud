package com.wenxianm.controller;

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
     * @param name
     * @author caiwx
     * @date 2021/11/3 - 16:37
     * @return SongSearchVO
     **/
    @GetMapping("/search")
    public SongSearchVO search(String name) {
        if (StringUtils.isBlank(name)) {
            return new SongSearchVO();
        }
        return songService.search(name.trim());
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
