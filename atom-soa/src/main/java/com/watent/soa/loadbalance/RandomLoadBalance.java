package com.watent.soa.loadbalance;

import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * 随机算法
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public NodeInfo doSelect(List<String> registryInfo) {

        Random random = new Random();
        int index = random.nextInt(registryInfo.size());
        String registry = registryInfo.get(index);

        JSONObject jsonObject = JSONObject.parseObject(registry);
        Collection values = jsonObject.values();
        JSONObject nodeJobj = new JSONObject();
        for (Object value : values) {
            nodeJobj = JSONObject.parseObject(value.toString());
        }
        JSONObject protocolJobj = nodeJobj.getJSONObject("protocol");
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setHost(null == protocolJobj.getString("host") ? "" : protocolJobj.getString("host"));
        nodeInfo.setHost(null == protocolJobj.getString("port") ? "" : protocolJobj.getString("port"));
        nodeInfo.setContextPath(null == protocolJobj.getString("contextPath") ? "" : protocolJobj.getString("contextPath"));

        return nodeInfo;
    }
}
