package com.wenxianm.mq;

import com.wenxianm.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName RetryConsume
 * @Author cwx
 * @Date 2021/10/26 16:28
 **/
@Component
@Slf4j
public class RetryConsume implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        log.info("messageExt: {}", JsonUtil.objToStr(messageExt));
        int reconsumeTimes = messageExt.getReconsumeTimes();
        log.error("[{}]消息处理失败次数：{}", messageExt.getMsgId(), reconsumeTimes);
        throw new RuntimeException(String.format("第%s次处理失败..",reconsumeTimes));
    }
}
