package com.watent.soa.registry;

import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 注册抽象
 *
 * @author Atom
 */
public interface BaseRegistry {

    /**
     * 注册
     *
     * @param param              参数
     * @param applicationContext 上下文
     * @return 结果
     */
    boolean registry(String param, ApplicationContext applicationContext);

    /**
     * @param id                 bean ID
     * @param applicationContext 上下文
     * @return 注册的实例
     */
    List<String> getRegistry(String id, ApplicationContext applicationContext);

}
