package com.wenxianm.api;

import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.MqMessageDto;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.model.param.SongParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * mq消息
 * @ClassName MqMessageApi
 * @Author cwx
 * @Date 2021/10/28 17:46
 **/
@Component
@FeignClient(name = "blog-main-service", path = "/mq/message/")
public interface MqMessageApi {

    /**
     * 更新mq消息
     * @param mqMessageDto
     * @author caiwx
     * @date 2021/10/28 - 17:49
     * @return
     **/
    @PostMapping(value = "update")
    void update(@RequestBody MqMessageDto mqMessageDto);
}
