package com.wenxianm.design.strategy.impl;

import com.wenxianm.design.strategy.IStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * 网易策略
 * @ClassName StrategyA
 * @Author cwx
 * @Date 2021/12/10 11:26
 **/
@Slf4j
public class NetEaseStrategy implements IStrategy {

    @Override
    public void doSomething() {
        log.info("NetEaseStrategy：使用网易策略");
    }
}
