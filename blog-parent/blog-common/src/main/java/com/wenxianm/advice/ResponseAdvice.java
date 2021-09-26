package com.wenxianm.advice;

import com.wenxianm.model.JsonResponse;
import com.wenxianm.model.ServerCode;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ClassName ResponseAdvice
 * @Author cwx
 * @Date 2021/9/24 17:55
 **/
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        serverHttpResponse.getHeaders().add("traceId", MDC.get("traceId"));
        JsonResponse jsonResponse = new JsonResponse(ServerCode.SUCCESS.getCode());
        jsonResponse.setData(o);
        jsonResponse.setTraceId(MDC.get("traceId"));
        return jsonResponse;
    }
}
