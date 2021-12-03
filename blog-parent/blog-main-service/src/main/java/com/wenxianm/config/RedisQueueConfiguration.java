package com.wenxianm.config;

import com.wenxianm.service.RedisQueueService;
import com.wenxianm.service.mq.IMqMessageService;
import com.wenxianm.service.song.ISongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName RedisQueueConfiguration
 * @Author cwx
 * @Date 2021/11/11 11:07
 **/
@Component
@Slf4j
public class RedisQueueConfiguration {

    @Autowired
    private ISongService songService;
    @Autowired
    private IMqMessageService mqMessageService;

    @PostConstruct
    public void init() {
        log.info("初始化RedisQueueConfiguration");
        songService.consumerWaitReptile();

        mqMessageService.consumerRedis();
    }
}
