package com.wenxianm.model.dto;

import com.wenxianm.model.enums.MqMessageStatusEnum;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * mq消息
 * @author caiwx
 * @date 2021/10/28 - 10:17
 **/
@Data
public class MqMessageDto implements Serializable {

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

    public MqMessageDto() {}

     public MqMessageDto(Integer type, int status, String data) {
         this.type = type;
         this.status = status;
         this.data = data;
     }

     public static MqMessageDto success(String mqId) {
         MqMessageDto mqMessageDto = new MqMessageDto();
         mqMessageDto.setMqId(mqId);
         mqMessageDto.setStatus(MqMessageStatusEnum.SUCCESS.getCode());
         mqMessageDto.setConsumeTime(new Date());
         mqMessageDto.setReturnMsg("消费成功");
         return mqMessageDto;
     }

     public static MqMessageDto fail(String mqId) {
         MqMessageDto mqMessageDto = new MqMessageDto();
         mqMessageDto.setMqId(mqId);
         mqMessageDto.setStatus(MqMessageStatusEnum.FAIL.getCode());
         mqMessageDto.setConsumeTime(new Date());
         mqMessageDto.setReturnMsg("消费失败");
         return mqMessageDto;
     }


}
