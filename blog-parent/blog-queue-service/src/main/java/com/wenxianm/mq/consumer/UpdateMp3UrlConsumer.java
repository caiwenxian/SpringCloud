package com.wenxianm.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wenxianm.annotation.MQIgnoreTag;
import com.wenxianm.api.MqMessageApi;
import com.wenxianm.api.SongApi;
import com.wenxianm.exception.BaseException;
import com.wenxianm.model.Constants;
import com.wenxianm.model.ServerCode;
import com.wenxianm.model.dto.MqMessageDto;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.mq.Mp3UrlMessage;
import com.wenxianm.model.mq.TopSongMessage;
import com.wenxianm.mq.MySink;
import com.wenxianm.service.RedisService;
import com.wenxianm.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 更新歌曲的mp3
 * @ClassName ReptileTopSongConsumer
 * @Author cwx
 * @Date 2021/10/25 17:41
 **/
@Component
@Slf4j
public class UpdateMp3UrlConsumer {

    @Autowired
    private SongApi songApi;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MqMessageApi mqMessageApi;

    @MQIgnoreTag
    @StreamListener(MySink.BLOG_MAIN_UPDATE_MP3_URL)
    public void onMessage(@Payload byte[] body, @Headers Map headers) {
        String str = new String(body, StandardCharsets.UTF_8);
        log.info("原消息体：{}", str);
        Mp3UrlMessage info = JSONObject.parseObject(str, Mp3UrlMessage.class);
        String msgId = headers.get("rocketmq_MESSAGE_ID") == null ? "" : headers.get("rocketmq_MESSAGE_ID").toString();
        String tag = headers.get("rocketmq_TAGS") == null ? "" : headers.get("rocketmq_TAGS").toString();
        log.info("消息id：{}，tag：{}，消息内容：{}", msgId, tag, info);
        try {
            String key = Constants.MQ_LOCK + info.getSongId();
            if (redisService.exists(key)) {
                log.info("[{}]重复消息，不消费", msgId);
                return;
            }
            redisService.setObject(key, info.getUrl(), 60L);
            SongDto songDto = new SongDto();
            songDto.setSongId(info.getSongId());
            songDto.setMp3Url(info.getUrl());
            songApi.updateMp3Url(songDto);
            log.info("消费消息成功，消息id：{}，消息内容：{}", msgId, JsonUtil.objToStr(info));
            mqMessageApi.update(MqMessageDto.success(msgId));
        } catch (Exception e) {
            log.error("消费消息失败，消息id：{}，消息内容：{}", msgId, JsonUtil.objToStr(info), e);
            throw new BaseException(ServerCode.ERROR, "消费消息失败");
        }

    }
}
