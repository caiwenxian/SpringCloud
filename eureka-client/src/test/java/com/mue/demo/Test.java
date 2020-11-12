package com.mue.demo;

import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年11月09日 14:58:00
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContextP) throws BeansException {
        applicationContext = applicationContextP;
    }

    @org.junit.Test
    public void test() {
        Person p = new Person();

        System.out.println("===========");
        Person bean = applicationContext.getBean(Person.class);
        bean.speak();
    }


}
