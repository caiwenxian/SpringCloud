package com.wenxianm.design.strategy.factory;

import com.wenxianm.design.strategy.IStrategy;
import com.wenxianm.design.strategy.ReptileTypeEnum;
import com.wenxianm.design.strategy.impl.KuGouStrategy;
import com.wenxianm.design.strategy.impl.NetEaseStrategy;

import java.util.HashMap;
import java.util.Map;

import static com.wenxianm.design.strategy.ReptileTypeEnum.KU_GOU;
import static com.wenxianm.design.strategy.ReptileTypeEnum.NET_EASE;

/**
 * 策略模式结合工厂方法
 * @ClassName StrategyFactory
 * @Author cwx
 * @Date 2021/12/10 11:49
 **/
public class StrategyFactory {

    /**
     * 方式一
     * @param reptileTypeEnum
     * @return
     */
    static public IStrategy chooseStrategy(ReptileTypeEnum reptileTypeEnum) {
        switch (reptileTypeEnum) {
            case NET_EASE:
                return new NetEaseStrategy();
            case KU_GOU:
                return new KuGouStrategy();
            default:
                return null;
        }
    }

    /**
     * 方式二
     */
    private static Map<ReptileTypeEnum, IStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put(NET_EASE, new NetEaseStrategy());
        strategyMap.put(KU_GOU, new KuGouStrategy());
    }

    static public IStrategy choose(ReptileTypeEnum reptileTypeEnum) {
        if(!strategyMap.containsKey(reptileTypeEnum)) {
            return null;
        }
        return strategyMap.get(reptileTypeEnum);
    }
}
