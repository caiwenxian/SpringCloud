package com.wenxianm.utils;

import java.util.UUID;

/**
 * 随机码获取工具
 * @author caiwx
 * @date 2021/10/19 - 10:35
 **/
public class RandomUtil {

    static public String getUid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
