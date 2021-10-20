package com.wenxianm.model.enums;

import lombok.Getter;


/**
 * 来源
 *
 * @Author: [caiwenxian]
 * @Date: [2018-01-27 11:05]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.bjike]
 */
@Getter
public enum SongOriginEnum {

    WANG_YI("网易", 1),

    KU_GOU("酷狗", 2);

    private String name;

    private Integer code;

    SongOriginEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    SongOriginEnum() {
    }

    static String getName(int code) {
        for (SongOriginEnum origin : SongOriginEnum.values()) {
            if (code == origin.getCode()) {
                return origin.name;
            }
        }
        return null;
    }
}
