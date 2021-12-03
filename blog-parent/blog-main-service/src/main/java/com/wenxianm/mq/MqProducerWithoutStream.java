package com.wenxianm.mq;

import com.wenxianm.model.enums.DelayTimeLevelEnum;
import com.wenxianm.model.enums.MQTagEnum;
import com.wenxianm.utils.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 不使用stream发送rocketmq
 * 可以获取到rocketmq返回的消息id
 * @ClassName MqProducerWithoutStream
 * @Author cwx
 * @Date 2021/10/28 15:30
 **/
@Service
@Slf4j
public class MqProducerWithoutStream {

    @Value("${spring.cloud.stream.rocketmq.binder.name-server}")
    private String nameServer;

    @Value("${spring.cloud.stream.rocketmq.bindings.output1.producer.group}")
    private String output1Group;

    @Value("${spring.cloud.stream.bindings.output1.destination}")
    private String output1Topic;

    private Producer output1;

    /**
     * 生成output1对应topic的producer
     * @author caiwx
     * @date 2021/10/28 - 15:49
     * @return Producer
     **/
    @PostConstruct
    public void initOutput1() throws MQClientException {
        log.info("initOutput1");
        DefaultMQProducer producer = new DefaultMQProducer(output1Group);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(6000);
        producer.start();
        this.output1 = new Producer(producer, output1Topic);
    }

    public Producer output1() {
        if (Objects.nonNull(this.output1)) {
            return output1;
        }
        DefaultMQProducer producer = new DefaultMQProducer(output1Group);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(6000);
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return new Producer(producer, output1Topic);
    }


    public class Producer {

        private DefaultMQProducer defaultMQProducer;

        private String topic;

        public Producer(DefaultMQProducer producer, String topic) {
            this.defaultMQProducer = producer;
            this.topic = topic;
        }

        /**
         * 发送同步消息
         * @param mqTagEnum tag
         * @param msg 消息体
         * @author caiwx
         * @date 2021/10/28 - 15:51
         * @return SendResult
         **/
        public MessageResponse send(Object msg, MQTagEnum mqTagEnum) {
            try {
//                defaultMQProducer.start();
                Message message = new Message(this.topic, mqTagEnum.getTag(), JsonUtil.objToStr(msg).getBytes());
                SendResult send = defaultMQProducer.send(message);
                log.info("sendResult: {}", JsonUtil.objToStr(send));
                return new MessageResponse(send);
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
                log.error("发送mq消息出错: ", e);
            } finally {
//                defaultMQProducer.shutdown();
            }
            return MessageResponse.fail();
        }

        /**
         * 发送延迟消息
         * @param msg 消息主体
         * @param mqTagEnum tag
         * @param delayTimeLevelEnum 延迟级别
         * @author caiwx
         * @date 2021/10/28 - 17:14
         * @return MessageResponse
         **/
        public MessageResponse send(Object msg, MQTagEnum mqTagEnum, DelayTimeLevelEnum delayTimeLevelEnum) {
            try {
//                defaultMQProducer.start();
                Message message = new Message(this.topic, mqTagEnum.getTag(), JsonUtil.objToStr(msg).getBytes());
                message.setDelayTimeLevel(delayTimeLevelEnum.getCode());
                SendResult send = defaultMQProducer.send(message);
                log.info("sendResult: {}", JsonUtil.objToStr(send));
                return new MessageResponse(send);
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
                log.error("发送mq消息出错: ", e);
            } finally {
//                defaultMQProducer.shutdown();
            }
            return MessageResponse.fail();
        }
    }

    @Data
    public static class MessageResponse extends SendResult{

        private SendResult sendResult;

        private String id;

        private Boolean success;


        public MessageResponse() {
        }

        public MessageResponse(SendResult sendResult) {
            this.sendResult = sendResult;
            if(sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                this.id = sendResult.getMsgId();
                this.success = true;
            } else {
                this.success = false;
            }

        }

        public static MessageResponse fail() {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setSuccess(false);
            return messageResponse;
        }
    }
}
