package com.wenxianm.design.chain;

import com.wenxianm.design.chain.factory.ChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test
 * @Author cwx
 * @Date 2021/12/13 11:27
 **/
@Slf4j
public class ChainTest {

    @Test
    public void test() {
        ReptileMp3Handler reptileMp3Handler = new ReptileMp3Handler();
        reptileMp3Handler.setNextHandler(new ReptileLyricHandler());
        reptileMp3Handler.doSomething("start chain");
    }

    @Test
    public void test2() {
        Handler handler = ChainFactory.buildChain();
        handler.doSomething("start chain");
    }

    @Test
    public void test3() {
        Handler handler = ChainFactory.buildChainV2();
        handler.doSomething("start chain");
    }

    @Test
    public void test4() {
        List<Thread> threadList = new ArrayList<>();
        int i = 0;
        while (i < 30) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                Handler handler = ChainFactory.buildChainV2();
                handler.doSomething("start chain: " + finalI);
            });
            threadList.add(thread);
            i ++;
        }
        threadList.forEach(v -> {
            v.run();
        });

    }

    @Test
    public void test5() {
        Handler handler = ChainFactory.buildChainV2();
        handler.doSomethingV2("start chain");
    }
}
