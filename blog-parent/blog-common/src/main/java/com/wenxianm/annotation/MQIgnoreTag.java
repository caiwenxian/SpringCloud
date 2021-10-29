package com.wenxianm.annotation;

import java.lang.annotation.*;

/**
 * mq消息忽略拦截
 * @author caiwx
 * @date 2021/10/26 - 15:25
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MQIgnoreTag {

}
