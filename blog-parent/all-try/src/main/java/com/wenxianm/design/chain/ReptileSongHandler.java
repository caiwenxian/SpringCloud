package com.wenxianm.design.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理链A1
 * @ClassName ReptileSongHandler
 * @Author cwx
 * @Date 2021/12/13 11:19
 **/
@Slf4j
public class ReptileSongHandler extends Handler{

    @Override
    public void doSomething(String str) {
        log.info("receive: {}，this is ReptileSongHandler", str);

        if (super.nextHandler != null) {
            super.nextHandler.doSomething("ReptileSongHandler");
        }
    }

    @Override
    public void doSomethingDiff(String str) {
        log.info("receive: {}，this is ReptileSongHandler", str);
    }
}
