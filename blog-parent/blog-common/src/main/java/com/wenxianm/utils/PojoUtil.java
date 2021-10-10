package com.wenxianm.utils;


import java.lang.invoke.SerializedLambda;

/**
 * @ClassName PojoUtil
 * @Author cwx
 * @Date 2021/10/9 16:35
 **/
public class PojoUtil {

    public static <T, R> String field(SerializedFunction<T, R> fn) {
        if (fn == null) {
            return null;
        }
        SerializedLambda serializedLambda = LambdaUtil.getSerializedLambda(fn);
        if (serializedLambda == null) {
            return null;
        }
        String methodName = serializedLambda.getImplMethodName();
        return PropertyNamer.methodToProperty(methodName);
    }
}
