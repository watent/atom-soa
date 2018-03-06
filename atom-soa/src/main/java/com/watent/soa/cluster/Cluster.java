package com.watent.soa.cluster;

import com.watent.soa.invoke.Invocation;

public interface Cluster {

    /**
     * 调用
     *
     * @param invocation 调用封装
     * @return 结果
     */
    String invoke(Invocation invocation) throws Exception;
}
