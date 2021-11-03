package com.wenxianm.model.entity;

import com.wenxianm.model.enums.MqTypeEnum;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * mq消息
 * @author caiwx
 * @date 2021/10/28 - 10:17
 **/
@Table(name = "t_mq_message")
@Data
public class MqMessage extends BaseEntity {

    private static final long serialVersionUID = -9018118238271043366L;

    /** 消息类型
     * @see com.wenxianm.model.enums.MqTypeEnum
     */
    private Integer type;

    /** 消息类型
     * @see com.wenxianm.model.enums.MqMessageTypeEnum
     */
    private Integer messageType;

    /**
     * 消息状态：0：初始状态、1：消费成功、-1、消费失败
     * @see com.wenxianm.model.enums.MqMessageStatusEnum
     */
    private Integer status;

    /** 消息主体，json串*/
    private String data;

    /** mq工具产生的消息id */
    private String mqId;

    /** 发送时间 */
    private Date sendTime;

    /** 消费时间 */
    private Date consumeTime;

    /** 消费后返回消息 */
    private String returnMsg;

    public MqMessage() {}

     public MqMessage(Integer type, int status, String data) {
         this.type = type;
         this.status = status;
         this.data = data;
     }

     /**
      * 初始化rocketmq消息
      * @param messageType
      * @param status
      * @param data
      * @param mqId
      * @author caiwx
      * @date 2021/10/28 - 10:41
      * @return MqMessage
      **/
     public static MqMessage initRocketMq(Integer messageType, Integer status, String data, String mqId) {
         MqMessage message = new MqMessage();
         message.setMessageType(messageType);
         message.setStatus(status);
         message.setData(data);
         message.setMqId(mqId);
         message.setSendTime(new Date());
         message.setType(MqTypeEnum.ROCKETMQ.getCode());
         return message;
     }

}
