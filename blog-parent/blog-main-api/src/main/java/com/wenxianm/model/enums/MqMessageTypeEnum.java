package com.wenxianm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * mq消息
 * @ClassName MqMessageTypeEnum
 * @Author cwx
 * @Date 2021/10/28 10:18
 **/
@Getter
@AllArgsConstructor
public enum MqMessageTypeEnum {

    REPTILE_TOP_SONG(1, "reptile 榜单"),

    UPDATE_MP3_URL(2, "更新歌曲的mp3");

    private Integer code;

    private String des;
}
