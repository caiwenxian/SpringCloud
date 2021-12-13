package com.wenxianm.design.strategy.context;

import com.wenxianm.design.strategy.IStrategy;

/**
 * @ClassName Context
 * @Author cwx
 * @Date 2021/12/10 11:32
 **/
public class Context {

    private IStrategy strategy;

    public Context(IStrategy strategy) {
        this.strategy = strategy;
    }

    public void soSomething() {
        strategy.doSomething();
    }
}
