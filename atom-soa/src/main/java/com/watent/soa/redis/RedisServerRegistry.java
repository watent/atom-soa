package com.watent.soa.redis;

import redis.clients.jedis.JedisPubSub;

/**
 * 发布订阅
 *
 * redis 发布订阅并发量低 大并需要MQ
 *
 * service reference 做 其他标签 生产者消费者都有
 *
 * @author Atom
 */
public class RedisServerRegistry extends JedisPubSub {

    // 当向 channel 队列发消息时  触发
    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);

        //更新  registryInfo
    }

    @Override
    public void subscribe(String... channels) {
        super.subscribe(channels);
    }
}
