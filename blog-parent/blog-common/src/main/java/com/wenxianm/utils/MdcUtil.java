package com.wenxianm.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName MdcUtil
 * @Author cwx
 * @Date 2021/9/25 17:36
 **/
@Slf4j
public class MdcUtil {

    public static final String LOG_TRACE_ID = "traceId";


    public static void setTraceIdIfAbsent() {
        if (MDC.get(LOG_TRACE_ID) == null) {
            MDC.put(LOG_TRACE_ID, getTraceId());
        }
    }

    public static void setTraceId() {
        MDC.put(LOG_TRACE_ID, getTraceId());
    }

    public static String getTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void setTraceId(String traceId) {
        MDC.put(LOG_TRACE_ID, traceId);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } catch (InterruptedException e) {
                log.error("ThreadPoolExecuteError", e);
                return null;
            } catch (ExecutionException e) {
                log.error("ThreadPoolExecuteError", e);
                return null;
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } catch (Exception e) {
                log.error("ThreadPoolExecuteError", e);
            } finally {
                MDC.clear();
            }
        };
    }
}
