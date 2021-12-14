package com.wenxianm.design.chain.factory;

import com.wenxianm.design.chain.Handler;
import com.wenxianm.design.chain.ReptileLyricHandler;
import com.wenxianm.design.chain.ReptileMp3Handler;
import com.wenxianm.design.chain.ReptileSongHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ChainFactory
 * @Author cwx
 * @Date 2021/12/13 11:43
 **/
@Slf4j
public class ChainFactory {

    private static Handler handler;

    /**
     * 方式一
     * 构造链路
     * @author caiwx
     * @date 2021/12/13 - 17:45
     * @return Handler
     **/
    public static Handler buildChain() {
        ReptileSongHandler reptileSongHandler = new ReptileSongHandler();
        reptileSongHandler.addNext(new ReptileMp3Handler());
        reptileSongHandler.addNext(new ReptileLyricHandler());
        return reptileSongHandler;
    }

    /**
     * 方式二
     * 构造链路
     * @author caiwx
     * @date 2021/12/13 - 17:45
     * @return Handler
     **/
    public static Handler buildChainV2() {
        if (handler != null) {
            return handler;
        }
        synchronized (ChainFactory.class) {
            if (handler == null) {
                handler = new ReptileSongHandler();
                handler.addNext(new ReptileMp3Handler());
                handler.addNext(new ReptileLyricHandler());
                return handler;
            }
            return handler;
        }
    }
}
