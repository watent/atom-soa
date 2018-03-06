package com.watent.soa.cluster;

import com.watent.soa.exception.SOAException;
import com.watent.soa.invoke.Invocation;
import com.watent.soa.invoke.Invoke;

/**
 * 调用失败就自动切换到其他节点
 *
 * @author Atom
 */
public class FailoverClusterInvoke implements Cluster {

    @Override
    public String invoke(Invocation invocation) throws Exception {

        String retries = invocation.getReference().getRetries();
        Integer retryInt = Integer.parseInt(retries);

        for (int i = 0; i < retryInt; i++) {
            try {

                Invoke invoke = invocation.getInvoke();
                String result = invoke.invoke(invocation);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        throw new SOAException("retry " + retries + "count all fail !!!");
    }
}
