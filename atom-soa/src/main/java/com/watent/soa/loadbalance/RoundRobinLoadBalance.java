package com.watent.soa.loadbalance;

import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * @author Atom
 */
public class RoundRobinLoadBalance implements LoadBalance {

    private static Integer index = 0;

    @Override
    public NodeInfo doSelect(List<String> registryInfo) {

        synchronized (index) {
            if (index >= registryInfo.size()) {
                index = 0;
            }
            String registry = registryInfo.get(index);
            index++;

            JSONObject jsonObject = JSONObject.parseObject(registry);
            Collection values = jsonObject.values();
            JSONObject nodeJobj = new JSONObject();
            for (Object value : values) {
                nodeJobj = JSONObject.parseObject(value.toString());
            }
            JSONObject protocolJobj = nodeJobj.getJSONObject("protocol");
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setHost(null == protocolJobj.getString("host") ? "" : protocolJobj.getString("host"));
            nodeInfo.setPort(null == protocolJobj.getString("port") ? "" : protocolJobj.getString("port"));
            nodeInfo.setContextPath(null == protocolJobj.getString("contextpath") ? "" : protocolJobj.getString("contextpath"));

            return nodeInfo;
        }
    }
}
