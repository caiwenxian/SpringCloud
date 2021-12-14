package com.wenxianm.design.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理链A
 * @ClassName ReptileMp3Handler
 * @Author cwx
 * @Date 2021/12/13 11:19
 **/
@Slf4j
public class ReptileMp3Handler extends Handler{

    @Override
    public void doSomething(String str) {
        log.info("receive: {}，this is ReptileMp3Handler", str);

        if (super.nextHandler != null) {
            super.nextHandler.doSomething("ReptileMp3Handler");
        }
    }

    @Override
    public void doSomethingDiff(String str) {
        log.info("receive: {}，this is ReptileMp3Handler", str);
    }
}
