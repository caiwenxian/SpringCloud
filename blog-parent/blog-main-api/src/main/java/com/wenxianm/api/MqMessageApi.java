package com.wenxianm.api;

import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.MqMessageDto;
import com.wenxianm.model.param.MqMessageParam;
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
@FeignClient(name = "blog-main-service", path = "/blog/mq/message/")
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

    /**
     * 获取mq消息分页列表
     * @param mqMessageParam
     * @author caiwx
     * @date 2021/10/28 - 17:49
     * @return PageData<MqMessageDto>
     **/
    @GetMapping(value = "page")
    PageData<MqMessageDto> listPage(MqMessageParam mqMessageParam);
}
