package com.wenxianm.model.mq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName Mp3UrlMessage
 * @Author cwx
 * @Date 2021/10/23 17:53
 **/
@Data
@Slf4j
public class Mp3UrlMessage implements Serializable {

    /**
     * 歌曲id
     */
    private Long songId;

    /**
     * MP3地址
     */
    private String url;
}
