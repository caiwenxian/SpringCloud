package com.wenxianm.design.strategy.context;

import com.wenxianm.design.strategy.StrategyImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @ClassName AStrategyContext
 * @Author cwx
 * @Date 2021/12/14 10:24
 **/
@Slf4j
@Component
public class StrategyContext {

    private Map<String, BiFunction<String, String, String>> strategyMap = new HashMap<>();

    @Autowired
    private StrategyImpl strategy;

    @PostConstruct
    public void init() {
        strategyMap.put("one", (arg1, arg2) -> strategy.doSomethingOne(arg1));
        strategyMap.put("two", (arg1, arg2) -> strategy.doSomethingTwo(arg1));
        strategyMap.put("three", (arg1, arg2) -> strategy.doSomethingThree(arg1));
    }

    public String doSomething(String strategyType, String param1, String param2) {
        BiFunction<String, String, String> stringStringStringBiFunction = strategyMap.get(strategyType);
        String apply = stringStringStringBiFunction.apply(param1, param2);
        return apply;
    }

}
