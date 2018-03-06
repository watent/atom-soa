package com.watent.soa.invoke;

import com.alibaba.fastjson.JSONArray;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static Method getMethod(Object bean, String methodName, JSONArray paramTypes) {

        Method[] methods = bean.getClass().getMethods();
        List<Method> retMethods = new ArrayList<>();

        for (Method method : methods) {
            //放入同名的
            if (method.getName().trim().equals(methodName)) {
                retMethods.add(method);
            }
        }

        if (retMethods.size() == 1) {
            return retMethods.get(0);
        }

        boolean isSameSize = false;
        boolean isSameType = false;
        tag:
        for (Method method : retMethods) {
            Class<?>[] types = method.getParameterTypes();
            if (types.length == paramTypes.size()) {
                isSameSize = true;
            }
            if (!isSameSize) {
                continue;
            }

            for (int i = 0; i < types.length; i++) {
                if (types[i].toString().contains(paramTypes.getString(i))) {
                    isSameType = true;
                } else {
                    isSameType = false;
                }
                if (!isSameType) {
                    continue tag;
                }
            }
            if (isSameType) {
                return method;
            }
        }
        return null;
    }
}
