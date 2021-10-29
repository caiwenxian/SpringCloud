package com.wenxianm.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @ClassName MySource
 * @Author cwx
 * @Date 2021/10/23 17:46
 **/
@Deprecated
public interface MySource {

    /**
     * mq出口
     * @author caiwx
     * @date 2021/10/23 - 17:48
     * @return MessageChannel
     **/

    //@Output("output1")
    //使用MqProducerWithoutStream来发送mq消息时，需要把这个注释，这个是stream的rocketmq，产生的代理类和MqProducerWithoutStream#output1()产生的DefaultMQProducer冲突
    MessageChannel output1();
}
