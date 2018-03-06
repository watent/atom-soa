atom-soa-parent
==============
    atom-soa-parent
    
    
集群容错

    failover    --自动切换到好的机器
    failfast    --节点调用失败 直接失败
    failsafe    --失败了就忽略
    
动态注册

    redis 发布订阅
    
服务发现与剔除
    
    redis       发布订阅
    failover    调用都失败剔除节点
    
step
    
    1.spring 整合 spring接口 自定义标签
    2.注册中心 redis 缓存
    3.动态代理 消费者拿到代理实例
    4.负载均衡
    5.调用洗衣 http rmi netty
    6.设计模式 策略 委托 观察 代理
    7.集群容错
    
    
    