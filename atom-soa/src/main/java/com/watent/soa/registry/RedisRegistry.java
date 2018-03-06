package com.watent.soa.registry;

import com.alibaba.fastjson.JSONObject;
import com.watent.soa.bean.Protocol;
import com.watent.soa.bean.Registry;
import com.watent.soa.bean.Service;
import com.watent.soa.exception.SOAException;
import com.watent.soa.redis.RedisApi;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 注册中心处理类
 * <p>
 * {
 * "testServiceImpl":{
 * "host:port":{
 * "protocol":{},
 * "service":{}
 * },
 * "host:port1":{
 * "protocol":{},
 * "service":{}
 * },
 * "host:port2":{
 * "protocol":{},
 * "service":{}
 * }
 * }
 * }
 *
 * @author Atom
 */
public class RedisRegistry implements BaseRegistry {

    @Override
    public boolean registry(String ref, ApplicationContext applicationContext) {

        try {
            Protocol protocol = applicationContext.getBean(Protocol.class);

            Registry registry = applicationContext.getBean(Registry.class);

            Map<String, Service> serviceMap = applicationContext.getBeansOfType(Service.class);

            RedisApi.createJedisPool(registry.getAddress());

            for (Map.Entry<String, Service> entry : serviceMap.entrySet()) {
                if (entry.getValue().getRef().equals(ref)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("protocol", JSONObject.toJSONString(protocol));
                    jsonObject.put("service", JSONObject.toJSONString(entry.getValue()));
                    jsonObject.put("protocol", JSONObject.toJSONString(protocol));

                    JSONObject ipPort = new JSONObject();
                    ipPort.put(protocol.getHost() + ":" + protocol.getPort(), jsonObject);

                    this.lpush(ipPort, ref);
                }
            }
            return true;
        } catch (Exception e) {
            throw new SOAException(e);
        }
    }

    private void lpush(JSONObject ipPort, String key) {

        if (RedisApi.exists(key)) {
            Set<String> keys = ipPort.keySet();
            String ipPortStr = "";
            //循环一次
            //"host:port":{
            //"protocol":{},
            //"service":{}
            //}
            for (String k : keys) {
                ipPortStr = k;
            }
            //redis 对应key 内容
            List<String> registryInfo = RedisApi.lrange(key);
            List<String> newRegistry = new ArrayList<>();

            boolean isOld = false;
            for (String node : registryInfo) {
                JSONObject jsonObject = JSONObject.parseObject(node);
                if (jsonObject.containsKey(ipPortStr)) {
                    newRegistry.add(ipPort.toString());
                    isOld = true;
                } else {
                    newRegistry.add(ipPort.toString());
                }
            }
            if (isOld) {
                //老机器去重
                if (newRegistry.size() > 0) {
                    RedisApi.del(key);
                    String[] newReStr = new String[newRegistry.size()];
                    for (int i = 0; i < newRegistry.size(); i++) {
                        newReStr[i] = newRegistry.get(i);
                    }
                    RedisApi.lpush(key, newReStr);
                }
            } else {
                //加入新启动机器
                RedisApi.lpush(key, ipPort.toJSONString());
            }
        } else {
            //都为第一次启动
            RedisApi.lpush(key, ipPort.toJSONString());
        }
    }

    @Override
    public List<String> getRegistry(String id, ApplicationContext applicationContext) {
        try {
            Registry registry = applicationContext.getBean(Registry.class);
            RedisApi.createJedisPool(registry.getAddress());
            if (RedisApi.exists(id)) {
                //key 对应的 list
                return RedisApi.lrange(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
