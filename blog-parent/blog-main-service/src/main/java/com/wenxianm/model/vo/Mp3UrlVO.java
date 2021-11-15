package com.wenxianm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName Mp3UrlVO
 * @Author cwx
 * @Date 2021/9/24 18:16
 **/
@Data
@AllArgsConstructor
public class Mp3UrlVO {

    /** 歌曲id */
    private Long songId;

    /** 名称 */
    private String mp3Url;


    public Mp3UrlVO() {
    }

}
