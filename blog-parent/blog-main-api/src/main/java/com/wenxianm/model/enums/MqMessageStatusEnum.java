package com.wenxianm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName MqMessageStatusEnum
 * @Author cwx
 * @Date 2021/10/28 10:24
 **/
@Getter
@AllArgsConstructor
public enum MqMessageStatusEnum {

    DOING(0, "初始状态"),

    SUCCESS(1, "消费成功"),

    FAIL(-1, "消费失败");

    private Integer code;

    private String des;

    public static MqMessageStatusEnum getByCode(Integer code) {
        for (MqMessageStatusEnum value : MqMessageStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
