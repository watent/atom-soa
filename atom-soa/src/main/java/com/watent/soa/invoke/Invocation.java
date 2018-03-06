package com.watent.soa.invoke;

import com.watent.soa.bean.Reference;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author Atom
 */
@Data
public class Invocation {

    /**
     * 方法
     */
    private Method method;
    /**
     * 参数
     */
    private Object[] objs;

    private Reference reference;

    private Invoke invoke;
}
