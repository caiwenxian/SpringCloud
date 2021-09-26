package com.wenxianm.aspect;

import com.wenxianm.utils.MdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * 请求切面
 * @ClassName ControllerAspect
 * @Author cwx
 * @Date 2021/9/24 17:17
 **/
@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Pointcut("@annotation(com.wenxianm.annotation.ApiMethod)")
    private void pointcut(){}

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        String apiName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.info("========== 调用接口:{} start ==========", apiName);
        for (Object arg : joinPoint.getArgs()) {
            log.info("arg:{}",arg);
        }
        log.info("========== 调用接口:{} end ==========", apiName);
    }

}
