package com.wenxianm.service.mq;

import com.wenxianm.model.PageData;
import com.wenxianm.model.dto.MqMessageDto;
import com.wenxianm.model.entity.MqMessage;
import com.wenxianm.model.param.MqMessageParam;

import java.util.List;

/**
 * mq消息
 * @ClassName IMqMessageService
 * @Author cwx
 * @Date 2021/10/28 10:46
 **/
public interface IMqMessageService {

    /**
     * 新增
     * @param message
     * @author caiwx
     * @date 2021/10/28 - 10:49
     **/
    void addOne(MqMessage message);

    /**
     * 更新
     * @param mqMessageDto
     * @author caiwx
     * @date 2021/10/28 - 10:50
     **/
    void updateOne(MqMessageDto mqMessageDto);

    /**
     * 获取单个
     * @param param
     * @author caiwx
     * @date 2021/10/28 - 10:55
     * @return MqMessageDto
     **/
    MqMessageDto getOne(MqMessageParam param);

    /**
     * 获取列表
     * @param param
     * @author caiwx
     * @date 2021/10/28 - 10:55
     * @return MqMessageDto
     **/
    List<MqMessageDto> list(MqMessageParam param);

    /**
     * 获取分页列表
     * @param param
     * @author caiwx
     * @date 2021/10/28 - 10:55
     * @return MqMessageDto
     **/
    PageData<MqMessageDto> listPage(MqMessageParam param);


}
