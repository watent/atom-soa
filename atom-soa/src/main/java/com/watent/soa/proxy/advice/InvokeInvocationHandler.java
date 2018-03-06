package com.watent.soa.proxy.advice;

import com.watent.soa.bean.Reference;
import com.watent.soa.cluster.Cluster;
import com.watent.soa.invoke.Invocation;
import com.watent.soa.invoke.Invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvokeInvocationHandler 这个是一个advice，在这个advice里面就进行了rpc的远程调用
 * rpc：http、rmi、netty
 *
 * @author Atom
 */

public class InvokeInvocationHandler implements InvocationHandler {

    private Invoke invoke;

    private Reference reference;

    public InvokeInvocationHandler(Invoke invoke, Reference reference) {
        this.invoke = invoke;
        this.reference = reference;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        //在这个invoke里面最终要调用多个远程的provider
        System.out.print("已经获取到了代理实例，已经掉到了InvokeInvocationHandler.invoke\n");
        Invocation invocation = new Invocation();
        invocation.setMethod(method);
        invocation.setObjs(args);
        invocation.setReference(reference);
//        String result = invoke.invoke(invocation);
        invocation.setInvoke(invoke);
        Cluster cluster = reference.getClusterMap().get(reference.getCluster());
        String result = cluster.invoke(invocation);
        return result;
    }
}
