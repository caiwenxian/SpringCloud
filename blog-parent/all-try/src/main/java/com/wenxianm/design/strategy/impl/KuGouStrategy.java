package com.wenxianm.design.strategy.impl;

import com.wenxianm.design.strategy.IStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * 酷狗策略
 * @ClassName KuGouStrategy
 * @Author cwx
 * @Date 2021/12/10 11:31
 **/
@Slf4j
public class KuGouStrategy implements IStrategy {

    @Override
    public void doSomething() {
        log.info("KuGouStrategy：使用酷狗策略");
    }
}
