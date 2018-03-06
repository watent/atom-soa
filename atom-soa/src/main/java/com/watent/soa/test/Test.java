package com.watent.soa.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Atom
 */
public class Test {

    public static void main(String[] args) {

        ApplicationContext app = new ClassPathXmlApplicationContext("test.xml");
//        TestService testService = app.getBean(TestService.class);
//        testService.eat("eat");
    }

}
