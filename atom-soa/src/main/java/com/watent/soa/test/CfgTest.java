package com.watent.soa.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Atom
 */
public class CfgTest {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("test.xml");

    }

}
