package com.wenxianm.mq;

import com.wenxianm.model.enums.DelayTimeLevelEnum;
import com.wenxianm.model.enums.MQTagEnum;
import com.wenxianm.utils.IDUtil;
import com.wenxianm.utils.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.Objects;

/**
 * @ClassName MqProducer
 * @Author cwx
 * @Date 2021/10/23 17:51
 * @Deprecated 由于使用stream来发送mq消息，获取不到mq返回的消息id，故废弃
 **/
@Service
@Slf4j
@Deprecated
public class MqProducer {

    @Autowired
    private MySource mySource;

    /**
     * 发送mq，没有序列化
     * @param message 消息体
     * @param mqTagEnum tag
     * @author caiwx
     * @date 2021/10/23 - 18:10
     **/
    public MessageResponse sendNoSerialize(Object message, MQTagEnum mqTagEnum) {
        log.info("发送mq消息，message：{}， tag：{}", JsonUtil.objToStr(message), mqTagEnum.getTag());
        if (Objects.isNull(message) || Objects.isNull(mqTagEnum)) {
            return MessageResponse.fail();
        }
        try {
            Message msg = MessageBuilder.withPayload(message)
                    .setHeader(MessageConst.PROPERTY_TAGS, mqTagEnum.getTag())
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .setHeader(MessageConst.PROPERTY_KEYS, IDUtil.random4mq())
                    .build();
            boolean send = mySource.output1().send(msg);
            log.info("发送mq消息结果：{}，msgId：{}， tag：{}", send, msg.getHeaders().getId(), mqTagEnum.getTag());
            return new MessageResponse(msg);
        } catch (Exception e) {
            log.error("发送mq出错：", e);
            log.info("发送mq消息失败，message：{}， tag：{}", JsonUtil.objToStr(message), mqTagEnum.getTag());
        }
        return MessageResponse.fail();
    }

    /**
     * 发送延迟mq，没有序列化
     * @param message 消息体
     * @param mqTagEnum tag
     * @param delayTimeLevelEnum 延迟级别
     * @author caiwx
     * @date 2021/10/27 - 10:29
     **/
    public MessageResponse sendDelayNoSerialize(Object message, MQTagEnum mqTagEnum, DelayTimeLevelEnum delayTimeLevelEnum) {
        log.info("发送mq消息，message：{}， tag：{}，level：{}", JsonUtil.objToStr(message), mqTagEnum.getTag(), delayTimeLevelEnum.getCode());
        if (Objects.isNull(message) || Objects.isNull(mqTagEnum) || Objects.isNull(delayTimeLevelEnum)) {
            return MessageResponse.fail();
        }
        try {
            Message msg = MessageBuilder.withPayload(message)
                    .setHeader(MessageConst.PROPERTY_TAGS, mqTagEnum.getTag())
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                    .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL,delayTimeLevelEnum.getCode())
                    .setHeader(MessageConst.PROPERTY_KEYS, IDUtil.random4mq())
                    .build();
            boolean send = mySource.output1().send(msg);
            log.info("发送mq消息结果：{}，msgId：{}， tag：{}", send, msg.getHeaders().getId(), mqTagEnum.getTag());
            return new MessageResponse(msg);
        } catch (Exception e) {
            log.error("发送mq出错：", e);
            log.info("发送mq消息失败，message：{}， tag：{}", JsonUtil.objToStr(message), mqTagEnum.getTag());
        }
        return MessageResponse.fail();
    }

    @Data
    public static class MessageResponse implements Message {

        private Message message;

        private String key;

        private String id;

        private Boolean success;

        @Override
        public Object getPayload() {
            return message.getPayload();
        }

        @Override
        public MessageHeaders getHeaders() {
            return message.getHeaders();
        }

        public MessageResponse() {
        }

        public MessageResponse(Message message) {
            this.message = message;
            this.key = String.valueOf(message.getHeaders().get(MessageConst.PROPERTY_KEYS));
            this.id = message.getHeaders().getId().toString();
            this.success = true;
        }

        public static MessageResponse fail() {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setSuccess(false);
            return messageResponse;
        }
    }

}
