package com.watent.soa.bean;

import com.watent.soa.redis.RedisApi;
import com.watent.soa.registry.BaseRegistryDelegate;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.Serializable;

/**
 * 服务
 *
 * @author Atom
 */
@Data
public class Service extends BaseConfigBean implements Serializable, InitializingBean, ApplicationContextAware {

    private String intf;

    private String ref;

    private String protocol;

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Service.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseRegistryDelegate.registry(ref, applicationContext);

        //*要与redis 节点里面内容一致
        RedisApi.publish("channel" + ref, "*");
    }

}
