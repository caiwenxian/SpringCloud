package com.wenxianm.design.strategy;

import com.wenxianm.design.strategy.context.Context;
import com.wenxianm.design.strategy.factory.StrategyFactory;
import com.wenxianm.design.strategy.impl.NetEaseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 策略模式测试
 * @ClassName StrategyTest
 * @Author cwx
 * @Date 2021/12/10 11:35
 **/
@Slf4j
public class StrategyTest {

    @Test
    public void test() {
        IStrategy strategy = new NetEaseStrategy();
        Context context = new Context(strategy);
        context.soSomething();
    }

    @Test
    public void test2() {
        ReptileTypeEnum reptileTypeEnum = ReptileTypeEnum.KU_GOU;
        StrategyFactory.chooseStrategy(reptileTypeEnum).doSomething();
    }

    @Test
    public void test3() {
        ReptileTypeEnum reptileTypeEnum = ReptileTypeEnum.KU_GOU;
        StrategyFactory.choose(reptileTypeEnum).doSomething();
    }
}
