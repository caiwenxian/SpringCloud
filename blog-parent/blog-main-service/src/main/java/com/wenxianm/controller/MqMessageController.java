package com.wenxianm.controller;

import com.wenxianm.annotation.ApiMethod;
import com.wenxianm.api.MqMessageApi;
import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.MqMessageDto;
import com.wenxianm.model.dto.SongDto;
import com.wenxianm.service.mq.IMqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MqMessageController
 * @Author cwx
 * @Date 2021/10/28 17:50
 **/
@RestController
@RequestMapping("/mq/message")
@Slf4j
public class MqMessageController implements MqMessageApi {

    @Autowired
    IMqMessageService mqMessageService;

    @ApiMethod
    @Override
    public void update(@RequestBody MqMessageDto mqMessageDto) {
        mqMessageService.updateOne(mqMessageDto);
    }
}
