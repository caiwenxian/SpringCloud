package com.wenxianm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName SongDto
 * @Author cwx
 * @Date 2021/9/24 18:16
 **/
@Data
public class SongDto {

    /** 名称 */
    private String name;

    public SongDto() {
    }

    public SongDto(String name) {
        this.name = name;
    }
}
