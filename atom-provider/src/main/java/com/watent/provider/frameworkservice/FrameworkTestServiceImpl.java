package com.watent.provider.frameworkservice;


/**
 * @author Atom
 */
public class FrameworkTestServiceImpl implements FrameworkTestService {

    @Override
    public String sleep(String param) {

        String str = "Provider ->FrameworkTestServiceImpl->sleep:" + param;
        System.out.println(str);
        return str;
    }
}
