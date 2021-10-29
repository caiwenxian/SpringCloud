package com.wenxianm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * mq延迟级别
 * @ClassName DelayTimeLevelEnum
 * @Author cwx
 * @Date 2021/10/27 10:32
 **/
@Getter
@AllArgsConstructor
public enum DelayTimeLevelEnum {

    ONE(1, "1s"),
    two(2, "5s"),
    THREE(3, "10s"),
    FOUR(4, "30s"),
    FIVE(5, "1m"),
    FIX(6, "2m"),
    SEVEN(7, "3m"),
    EIGHT(8, "4m"),
    NIGH(9, "5m"),
    TEEN(10, "6m"),
    ELEVEN(11, "7m"),
    TWELVE(12, "8m"),
    THIRTEEN(13, "9m"),
    FOURTEEN(14, "10m"),
    FIFTEEN(15, "20m"),
    SIXTEEN(16, "30m"),
    SEVENTEEN(17, "1h"),
    EIGHTEEN(18, "2h");

    private Integer code;

    private String des;

    /**
     * 根据code获取
     * @param code
     * @author caiwx
     * @date 2021/10/27 - 10:48
     * @return DelayTimeLevelEnum
     **/
    public static DelayTimeLevelEnum getDelayTimeLevel(Integer code) {
        for (DelayTimeLevelEnum value : DelayTimeLevelEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
