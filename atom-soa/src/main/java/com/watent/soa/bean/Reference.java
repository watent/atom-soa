package com.watent.soa.bean;

import com.watent.soa.cluster.Cluster;
import com.watent.soa.cluster.FailfastClusterInvoke;
import com.watent.soa.cluster.FailoverClusterInvoke;
import com.watent.soa.cluster.FailsafeClusterInvoke;
import com.watent.soa.invoke.HttpInvoke;
import com.watent.soa.invoke.Invoke;
import com.watent.soa.invoke.NettyInvoke;
import com.watent.soa.invoke.RmiInvoke;
import com.watent.soa.loadbalance.LoadBalance;
import com.watent.soa.loadbalance.RandomLoadBalance;
import com.watent.soa.loadbalance.RoundRobinLoadBalance;
import com.watent.soa.proxy.advice.InvokeInvocationHandler;
import com.watent.soa.redis.RedisApi;
import com.watent.soa.redis.RedisServerRegistry;
import com.watent.soa.registry.BaseRegistryDelegate;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 引用
 *
 * @author Atom
 */
@Data
public class Reference implements Serializable, FactoryBean, ApplicationContextAware, InitializingBean {

    private String id;

    private String intf;

    private String loadbalance;

    private String protocol;

    private Invoke invoke;

    private String retries;

    private String cluster;

    private ApplicationContext applicationContext;

    public Reference() {
        System.out.println("Reference constructor");
    }

    private static List<String> registryInfo = new ArrayList<>();

    private static Map<String, Invoke> invokeMap = new HashMap<>();

    private static Map<String, LoadBalance> loadBalanceMap = new HashMap<>();

    private static Map<String, Cluster> clusterMap = new HashMap<>();

    static {
        invokeMap.put("http", new HttpInvoke());
        invokeMap.put("rmi", new RmiInvoke());
        invokeMap.put("netty", new NettyInvoke());

        loadBalanceMap.put("random", new RandomLoadBalance());
        loadBalanceMap.put("roundrob", new RoundRobinLoadBalance());

        clusterMap.put("failover", new FailoverClusterInvoke());
        clusterMap.put("failfast", new FailfastClusterInvoke());
        clusterMap.put("failsafe", new FailsafeClusterInvoke());
    }

    public List<String> getRegistryInfo() {
        return registryInfo;
    }

    public Map<String, LoadBalance> getLoadBalanceMap() {
        return loadBalanceMap;
    }

    public Map<String, Cluster> getClusterMap() {
        return clusterMap;
    }

    /**
     * 拿到一个实例 spring 初始化 getBean()调用
     * 返回值交给 IOC 容器管理
     * 返回 intf 接口代理
     */
    @Override
    public Object getObject() throws Exception {

        System.out.println("返回 inf 代理对象");
        if (!StringUtils.isEmpty(protocol)) {
            invoke = invokeMap.get(protocol);
        } else {
            Protocol protocol = applicationContext.getBean(Protocol.class);
            if (null != protocol) {
                invoke = invokeMap.get(protocol.getName());
            } else {
                invoke = invokeMap.get("http");
            }
        }
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Class.forName(intf)}, new InvokeInvocationHandler(invoke, this));

    }

    /**
     * 类的类型
     */
    @Override
    public Class<?> getObjectType() {

        if (!StringUtils.isEmpty(intf)) {
            try {
                return Class.forName(intf);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registryInfo = BaseRegistryDelegate.getRegistry(id, applicationContext);
        System.out.println(registryInfo);

        // 完成redis 订阅 channel与publisher一致  RedisServerRegistry.onMessage()接收消息
        RedisApi.subsribe("channel"+id,new RedisServerRegistry());
    }

}
