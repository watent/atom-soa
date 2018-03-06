package com.watent.soa.registry;

import com.watent.soa.bean.Registry;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 委托 注册
 *
 * @author Atom
 */
public class BaseRegistryDelegate {

    public static void registry(String ref, ApplicationContext applicationContext) {

        Registry registry = applicationContext.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        registryBean.registry(ref, applicationContext);

    }

    public static List<String> getRegistry(String id, ApplicationContext applicationContext) {

        Registry registry = applicationContext.getBean(Registry.class);

        String protocol = registry.getProtocol();

        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);

        return registryBean.getRegistry(id, applicationContext);
    }

}
