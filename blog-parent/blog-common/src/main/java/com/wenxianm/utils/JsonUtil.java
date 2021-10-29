package com.wenxianm.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @ClassName JsonUtil
 * @Author cwx
 * @Date 2021/10/23 18:19
 **/
public class JsonUtil {

    /**
     * 定制化值序列化处理
     */
    private static final SerializeConfig CONFIG = new SerializeConfig();
    static {
        CONFIG.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        CONFIG.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        CONFIG.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 功能：把json格式的字符串转化成Row
     * 详细：
     * @param json
     * @return
     * @author tanghuang 2016年10月23日
     */
    public static Map stringToMap(String json) {
        if (json == null || json.length() == 0) {
            return new HashMap();
        }
        return JSON.parseObject(json, Map.class);
    }

    /**
     * 功能：把Map转化成json格式的字符串<br>
     * 详细：<font color='red'>注意：去掉属性会将原map的键值remove掉</font>
     * @param map
     * @param excludeProperty 要去掉的属性
     * @return
     * @author tanghuang 2016年10月23日
     */
    public static String mapToString(Map map, String[] excludeProperty) {
        if (map == null || map.size() == 0) {
            return "{}";
        }
        if(excludeProperty!=null && excludeProperty.length>0){
            for(String key:excludeProperty){
                map.remove(key);
            }
        }
        return objToStr(map);
    }

    /**
     * 功能：map转对象
     *
     * 详细：
     *
     * @param map
     * @param clazz
     * @return
     * @author lizhifeng 2017年2月16日
     */
    public static <T> T mapToObj(Map map, Class<T> clazz) {
        return stringToObj(mapToString(map, null), clazz);
    }

    /**
     * 功能：把Map转化成json格式的字符串
     * 详细：
     * @param map
     * @return
     * @author tanghuang 2016年10月23日
     */
    public static String mapToString(Map map) {
        return mapToString(map, null);
    }

    /**
     * 功能：json转对象
     * 详细：
     * @param json
     * @param clazz
     * @return
     * @author tanghuang 2016年10月23日
     */
    public static <T> T stringToObj(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 功能：json转数组
     * 详细：
     * @param json
     * @return
     * @author tanghuang 2016年10月23日
     */
    public static Object[] stringToArr(String json){
        JSONArray jsonArr = JSON.parseArray(json);
        return jsonArr.toArray();
    }

    /**
     * 功能：把Object转化成json格式的字符串
     * 详细：
     * @param obj
     * @return
     * @author tanghuang 2016年10月23日
     */
    public static String objToStr(Object obj) {
        if (obj == null) {
            return "{}";
        }
        return JSON.toJSONString(obj, CONFIG, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 功能：把一个JSON对象数组描述字符转化为List
     * 详细：
     * @param json
     * @return
     * @author tanghuang 2016年10月23日
     */
    @SuppressWarnings("unchecked")
    public static List<Map> stringToList(String json) {
        return (List<Map>)stringToObjList(json, Map.class);
    }


    /**
     * 功能：把一个JSON对象数组描述字符转化为List
     * 详细：
     * @param json
     * @return
     * @author zhongshenghua 2018年08月24日
     */
    @SuppressWarnings("unchecked")
    public static List<String> stringToStringList(String json) {
        return (List<String>)stringToObjList(json, String.class);
    }

    /**
     * 功能：把一个JSON对象数组描述字符转化为List
     * 详细：
     * @param json
     * @param clazz
     * @return
     * @author tanghuang 2016年10月23日
     */
    public static List<?> stringToObjList(String json, Class<?> clazz) {
        return (List<?>)JSON.parseArray(json, clazz);
    }


}
