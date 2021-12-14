package com.wenxianm.design.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理链B
 * @ClassName ReptileLyricHandler
 * @Author cwx
 * @Date 2021/12/13 11:19
 **/
@Slf4j
public class ReptileLyricHandler extends Handler {

    @Override
    public void doSomething(String str) {
        log.info("receive: {}，this is ReptileLyricHandler", str);

        if (super.nextHandler != null) {
            super.nextHandler.doSomething("ReptileLyricHandler");
        }
    }

    @Override
    public void doSomethingDiff(String str) {
        log.info("receive: {}，this is ReptileLyricHandler", str);
    }
}
