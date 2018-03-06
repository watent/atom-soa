package com.watent.soa.cluster;

import com.watent.soa.invoke.Invocation;
import com.watent.soa.invoke.Invoke;

/**
 * 调用失败直接忽略
 *
 * @author Atom
 */
public class FailsafeClusterInvoke implements Cluster{

    @Override
    public String invoke(Invocation invocation) throws Exception {
        Invoke invoke = invocation.getInvoke();
        try {
            return invoke.invoke(invocation);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failsafe ignore";
        }
    }
}
