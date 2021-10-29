package com.wenxianm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * mq类型
 * @ClassName MqTypeEnum
 * @Author cwx
 * @Date 2021/10/28 10:18
 **/
@Getter
@AllArgsConstructor
public enum MqTypeEnum {

    ROCKETMQ(1, "rocketmq");

    private Integer code;

    private String des;
}
