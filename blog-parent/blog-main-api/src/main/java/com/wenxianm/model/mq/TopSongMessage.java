package com.wenxianm.model.mq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName TopSongMessage
 * @Author cwx
 * @Date 2021/10/23 17:53
 **/
@Data
@Slf4j
public class TopSongMessage implements Serializable {

    /**
     * 榜单id
     */
    private Long id;

    /**
     * 榜单url
     */
    private String url;
}
