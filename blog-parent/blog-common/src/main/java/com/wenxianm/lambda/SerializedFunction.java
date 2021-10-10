package com.wenxianm.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的函数接口
 *
 * @author ZhangWang
 * @version 1.0
 * @date 2021-01-22 14:48
 */
@FunctionalInterface
public interface SerializedFunction<T, R> extends Function<T, R>, Serializable {

}
