package com.wenxianm.aspect;

import com.google.common.collect.Lists;
import com.wenxianm.annotation.MQIgnoreTag;
import com.wenxianm.model.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 获取配置中需要忽略的mq消息，直接返回成功，不走业务逻辑
 * spring.cloud.stream.ignore-msg-id配置，多个用,隔开
 * @author caiwx
 * @date 2021/10/26 - 15:25
 **/
@Aspect
@Component
@Slf4j
public class MqIgnoreTagAspect {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 需要忽略的mq msgId
     */
    private static final String PROPERTY = "spring.cloud.stream.ignore-msg-id";


    @Pointcut("@annotation(com.wenxianm.annotation.MQIgnoreTag)")
    public void point() {
    }

    @Around(value = "point() && @annotation(annotation)")
    public Object mqIgnoreHandle(ProceedingJoinPoint pjp, MQIgnoreTag annotation) throws Throwable {
        String propertyValue = this.applicationContext.getEnvironment().getProperty(PROPERTY);
        log.debug(PROPERTY + ":" + propertyValue);
        if (!StringUtils.isEmpty(propertyValue)) {
            List<String> ignoreMsgIds = Lists.newArrayList(propertyValue.split(","));
            // 获取当前mq消息的msgId
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            Object[] args = pjp.getArgs();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            String msgId = "";
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation an : parameterAnnotations[i]) {
                    if (Objects.equals(an.annotationType().getTypeName(), "org.springframework.messaging.handler.annotation.Headers")) {
                        msgId = ((Map) args[i]).get(Constants.ROCKET_MQ_MESSAGE_ID).toString();
                        break;
                    }
                }
                if (!StringUtils.isEmpty(msgId)) {
                    break;
                }
            }
            if (ignoreMsgIds.contains(msgId)) {
                log.info("ignore mq msgId:{}", msgId);
                return null;
            }
        }
        return pjp.proceed();
    }
}
