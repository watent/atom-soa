package com.watent.soa.invoke;

import com.alibaba.fastjson.JSONObject;
import com.watent.soa.bean.Reference;
import com.watent.soa.loadbalance.LoadBalance;
import com.watent.soa.loadbalance.NodeInfo;
import com.watent.soa.rpc.http.HttpRequest;
import jdk.nashorn.internal.runtime.ECMAException;

import java.util.List;

/**
 * http的调用
 *
 * @author Atom
 */

public class HttpInvoke implements Invoke {

    @Override
    public String invoke(Invocation invocation) throws Exception {

        try {
            List<String> registryInfo = invocation.getReference().getRegistryInfo();
            //这个是负载均衡算法
            String loadbalance = invocation.getReference().getLoadbalance();
            Reference reference = invocation.getReference();
            LoadBalance loadbalanceBean = reference.getLoadBalanceMap().get(loadbalance);
            NodeInfo nodeInfo = loadbalanceBean.doSelect(registryInfo);

            //我们调用远程的生产者是传输的json字符串
            //根据serviceid去对端生产者的spring容器中获取serviceid对应的实例
            //根据methodName和methodType获取实例的method对象
            //然后反射调用method方法
            JSONObject sendParamJobj = new JSONObject();
            sendParamJobj.put("methodName", invocation.getMethod().getName());
            sendParamJobj.put("methodParams", invocation.getObjs());
            sendParamJobj.put("serviceId", reference.getId());
            sendParamJobj.put("paramTypes", invocation.getMethod().getParameterTypes());
            // http://127.0.0.1:8080/test/soa
            String url = "http://" + nodeInfo.getHost() + ":" + nodeInfo.getPort() + nodeInfo.getContextPath();

            //调用生产者的服务
            String result = HttpRequest.sendPost(url, sendParamJobj.toJSONString());

            return result;
        } catch (Exception e) {
            throw e;
        }
    }

}
