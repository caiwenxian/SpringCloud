package com.wenxianm.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * mq tag定义
 * @ClassName EnumMQTag
 * @Author cwx
 * @Date 2021/10/23 17:56
 **/
@Getter
@AllArgsConstructor
public enum MQTagEnum {

    BLOG_MAIN_REPTILE_TOP_SONG("blog_main_reptile_top_song", "reptile榜单消息"),

    BLOG_TEST("blog_test", "测试消息"),

    BLOG_MAIN_UPDATE_MP3_URL("blog_main_update_mp3_url", "更新歌曲mp3地址"),
    ;

    private String tag;

    private String des;
}
