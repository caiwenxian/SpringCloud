package com.wenxianm.design.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * 顶层处理器
 * @ClassName Handler
 * @Author cwx
 * @Date 2021/12/13 11:03
 **/
@Slf4j
public abstract class Handler {

    protected Handler nextHandler;

    /**
     * 设置下一步节点
     * @param nextHandler 下一步节点
     * @author caiwx
     * @date 2021/12/13 - 15:13
     **/
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 将处理节点添加到链中
     * @param nextHandler 下一步节点
     * @author caiwx
     * @date 2021/12/13 - 15:12
     **/
    public void addNext(Handler nextHandler) {
        if (nextHandler == null) {
            return;
        }
        if (this.nextHandler == null) {
            this.nextHandler = nextHandler;
            return;
        }
        this.nextHandler.addNext(nextHandler);
    }

    /**
     * 第一种节点处理方式，链路维护在各个节点
     * 业务接口
     * @param str
     * @author caiwx
     * @date 2021/12/13 - 11:14
     * @return 
     **/
    public abstract void doSomething(String str);

    /**
     * 第二种处理方式，链路维护在顶层方法
     * 顶层方法，处理整个链路
     * @param str
     * @author caiwx
     * @date 2021/12/13 - 15:35
     **/
    public void doSomethingV2(String str) {
        doSomethingDiff(str);
        if (this.nextHandler !=  null) {
            this.nextHandler.doSomethingV2(str);
        }
    }

    /**
     * 各个处理节点的动作
     * @param str
     * @author caiwx
     * @date 2021/12/13 - 15:35
     **/
    public abstract void doSomethingDiff(String str);

}
