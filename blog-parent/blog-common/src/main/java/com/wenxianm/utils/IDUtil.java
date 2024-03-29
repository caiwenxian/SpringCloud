package com.wenxianm.utils;

import java.util.Date;
import java.util.Random;

/**
 * id生成器
 * @ClassName IDUtil
 * @Author cwx
 * @Date 2021/10/12 18:16
 **/
public class IDUtil {

    private static final SnowflakeIdWorker SNOWFLAKE_ID_WORKER = new SnowflakeIdWorker(1L, 0L);

    /**
     * 雪花算法生成随机id
     * @author caiwx
     * @date 2021/10/12 - 18:17
     * @return Long
     **/
    public static Long random() {
        return SNOWFLAKE_ID_WORKER.nextId();
    }

    /**
     * 雪花算法生成随机id
     * @author caiwx
     * @date 2021/10/19 - 17:08
     * @return String
     **/
    public static String random4s() {
        Long id = SNOWFLAKE_ID_WORKER.nextId();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(DateUtil.get(new Date(), DateUtil.Field.YEAR));
        stringBuffer.append(DateUtil.get(new Date(), DateUtil.Field.MONTH));
        stringBuffer.append(id);
        return stringBuffer.toString();
    }

    /**
     * 雪花算法生成随机id
     * @author caiwx
     * @date 2021/10/19 - 17:08
     * @return String
     **/
    public static String random4mq() {
        Long id = SNOWFLAKE_ID_WORKER.nextId();
        StringBuffer stringBuffer = new StringBuffer("MQ");
        stringBuffer.append(DateUtil.get(new Date(), DateUtil.Field.YEAR));
        stringBuffer.append(DateUtil.get(new Date(), DateUtil.Field.MONTH));
        stringBuffer.append(id);
        return stringBuffer.toString();
    }

    /**
     * 返回范围内随机数
     * @param min
     * @param max
     * @author caiwx
     * @date 2021/11/4 - 17:53
     * @return Integer
     **/
    public static Integer randomInRange(Integer min, Integer max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


}
