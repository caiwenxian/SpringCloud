package com.wenxianm.interceptor;

import com.wenxianm.utils.MdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @ClassName CommonInterceptor
 * @Author cwx
 * @Date 2021/9/25 17:46
 **/
@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String traceId = httpServletRequest.getHeader(MdcUtil.LOG_TRACE_ID);
        if (Objects.isNull(traceId)) {
            traceId = MdcUtil.getTraceId();
        }
        MDC.put(MdcUtil.LOG_TRACE_ID, traceId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        MDC.remove(MdcUtil.LOG_TRACE_ID);
    }
}
