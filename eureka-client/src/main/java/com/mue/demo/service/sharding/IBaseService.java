package com.mue.demo.service.sharding;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * service基类。
 *
 * @param <T> model
 * @author 41742
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 暴露warpper lambda，默认disable为0。
     *
     * @return lambda。
     * @deprecated typo，请使用 {@link #enableLambda()} 代替
     */
    @Deprecated
    default LambdaQueryWrapper<T> defultLambda() {
        LambdaQueryWrapper<T> lambda = new QueryWrapper<T>()
            .eq("disable", 0)
            .lambda();
        return lambda;
    }

    /**
     * 暴露wrapper lambda，默认disable为0。
     *
     * @return lambda。
     */
    default LambdaQueryWrapper<T> enableLambda() {
        return new QueryWrapper<T>()
            .eq("disable", 0)
            .lambda();
    }

    /**
     * 暴露warpper lambda。
     *
     * @return lambda。
     */
    default LambdaQueryWrapper<T> lambda() {
        LambdaQueryWrapper<T> lambda = new QueryWrapper<T>()
            .lambda();
        return lambda;
    }

}
