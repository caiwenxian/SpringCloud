package com.mue.demo;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Person implements BeanNameAware {

    private String name;

    /**
     * 实现类上的override方法
     * @param s
     */
    @Override
    public void setBeanName(String s) {
        System.out.println("调用BeanNameAware中的setName赋值");
    }

    public Person() {
        System.out.println("构造方法");
    }

    /**
     * 属性赋值
     * @param name
     */
    public void setName(String name) {
        System.out.println("设置对象属性setName()..");
        this.name = name;
    }

    /**
     * Bean初始化
     */
    public void initBeanPerson() {
        System.out.println("初始化Bean");
    }

    /**
     * Bean方法使用：说话
     */
    public void speak() {
        System.out.println("使用Bean的Speak方法");
    }

    /**
     * 销毁Bean
     */
    public void destroyBeanPerson() {
        System.out.println("销毁Bean");
    }



    public static void main(String[] str) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("application.xml2");
        Person person = (Person)classPathXmlApplicationContext.getBean("person");
        person.speak();
        classPathXmlApplicationContext.close();
    }
}