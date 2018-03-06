package com.watent.soa.invoke;

/**
 * 返回String Json 方式通信
 *
 * @author Atom
 */
public interface Invoke {

    /**
     * 调用
     *
     * @param invocation  调用参数
     * @return 结果
     */
    String invoke(Invocation invocation) throws Exception;
}
