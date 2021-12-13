package com.wenxianm.design.strategy;

/**
 * @ClassName ReptileTypeEnum
 * @Author cwx
 * @Date 2021/12/10 11:51
 **/
public enum ReptileTypeEnum {

    NET_EASE(1, "网易"),

    KU_GOU(2, "酷狗");

    private Integer code;

    private String des;

    ReptileTypeEnum() {
    }

    ReptileTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

}
