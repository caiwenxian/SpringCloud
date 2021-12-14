package com.wenxianm.design.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 具体的策略实现
 * @ClassName StrategyImpl
 * @Author cwx
 * @Date 2021/12/14 10:49
 **/
@Component
@Slf4j
public class StrategyImpl {

    public String doSomethingOne(String str) {
        log.info("doSomethingOne: {}", str);
        return "";
    }

    public String doSomethingTwo(String str) {
        log.info("doSomethingTwo: {}", str);
        return "";
    }

    public String doSomethingThree(String str) {
        log.info("doSomethingThree: {}", str);
        return "";
    }
}
