package com.watent.soa.bean;

import com.watent.soa.netty.NettyUtil;
import com.watent.soa.rmi.RmiUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.Serializable;

/**
 * 协议
 *
 * @author Atom
 */
@Data
public class Protocol implements Serializable, InitializingBean, ApplicationListener<ContextRefreshedEvent> {

    private String name;

    private String host;

    private String port;

    private String contextpath;

    @Override
    public void afterPropertiesSet() throws Exception {
        if ("rmi".equalsIgnoreCase(name)) {
            RmiUtil rmi = new RmiUtil();
            rmi.startRmiServer(host, port, "soarmi");
        }
    }

    //spring 启动成功触发事件
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        System.out.println("-----------onApplicationEvent:" + event.getClass().getName() + "-----------");
        if (!ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {
            return;
        }
        if (!"netty".equalsIgnoreCase(name)) {
            return;
        }

        new Thread(() -> {
            try {
                NettyUtil.startServer(host, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
