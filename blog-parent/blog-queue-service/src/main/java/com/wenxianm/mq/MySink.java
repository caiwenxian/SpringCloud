package com.wenxianm.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @ClassName MySink
 * @Author cwx
 * @Date 2021/10/25 17:36
 **/

public interface MySink {

    public static final String BLOG_MAIN_REPTILE_TOP_SONG = "blog_main_reptile_top_song";

    public static final String BLOG_MAIN_UPDATE_MP3_URL = "blog_main_update_mp3_url";

    public static final String BLOG_TEST = "blog_test";

    /**
     * reptile榜单
     * @author caiwx
     * @date 2021/10/25 - 17:37
     * @return SubscribableChannel
     **/
    @Input(BLOG_MAIN_REPTILE_TOP_SONG)
    SubscribableChannel blogMainReptileTopSong();

    /**
     * test
     * @author caiwx
     * @date 2021/10/25 - 17:37
     * @return SubscribableChannel
     **/
    @Input(BLOG_TEST)
    SubscribableChannel blogTest();

    /**
     * 更新歌曲mp3
     * @author caiwx
     * @date 2021/10/25 - 17:37
     * @return SubscribableChannel
     **/
    @Input(BLOG_MAIN_UPDATE_MP3_URL)
    SubscribableChannel blogMainUpdateMp3Url();

}
