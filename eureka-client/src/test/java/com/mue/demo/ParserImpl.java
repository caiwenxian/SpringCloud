package com.mue.demo;

/**
 * @ClassName ParserImpl
 * @Description TODO
 * @Author cwx
 * @Date 2021/9/6 16:55
 * @Version 1.0
 **/
public class ParserImpl implements Parser{

    private static Action DO_NO_THING = new Action() {
        @Override
        public void doSomething() {

        }
    };


    @Override
    public Action findAction(Integer value) {
        if (value < 0) {
           return DO_NO_THING;
        }
        return null;
    }



}
