package com.watent.soa.loadbalance;

import java.util.List;

public interface LoadBalance {

    NodeInfo doSelect(List<String> registryInfo);
}
