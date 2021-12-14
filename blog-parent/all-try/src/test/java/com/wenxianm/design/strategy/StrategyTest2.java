package com.wenxianm.design.strategy;

import com.wenxianm.design.Application;
import com.wenxianm.design.strategy.context.StrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName StrategyTest2
 * @Author cwx
 * @Date 2021/12/14 11:00
 **/
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class StrategyTest2 {

    @Autowired
    private StrategyContext strategyContext;

    @Test
    public void test() {
        strategyContext.doSomething("one", "param1", "param2");
        strategyContext.doSomething("two", "param1", "param2");
    }
}
