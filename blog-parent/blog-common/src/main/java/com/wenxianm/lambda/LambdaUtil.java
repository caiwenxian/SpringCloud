package com.wenxianm.lambda;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

/**
 * Lambda工具类
 *
 * @author ZhangWang
 * @version 1.0
 * @date 2021-01-22 14:45
 */
@Slf4j
public class LambdaUtil {

    private static Cache<Class<?>, SerializedLambda> cache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .weakKeys()
            .build();

    /**
     * 获取函数接口的序列化Lambda
     *
     * @param fn 接口
     * @return 序列化的Lambda，获取失败时返回<code>null</code>
     * @author ZhangWang
     * @date 2021-01-22 15:32
     **/
    public static <T, R> SerializedLambda getSerializedLambda(SerializedFunction<T, R> fn) {
        try {
            return cache.get(fn.getClass(), () -> {
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                return (SerializedLambda) method.invoke(fn);
            });
        } catch (ExecutionException e) {
            log.error("获取[{}]的序列化接口失败", fn.getClass().getName());
            return null;
        }
    }
}
