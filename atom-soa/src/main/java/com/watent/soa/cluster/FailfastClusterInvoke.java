package com.watent.soa.cluster;

import com.watent.soa.invoke.Invocation;
import com.watent.soa.invoke.Invoke;

/**
 * 调用异常直接失败
 *
 * @author Atom
 */
public class FailfastClusterInvoke implements Cluster {

    @Override
    public String invoke(Invocation invocation) throws Exception {

        Invoke invoke = invocation.getInvoke();
        try {

            return invoke.invoke(invocation);
        } catch (Exception e) {
            throw e;
        }
    }
}
