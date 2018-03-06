package com.watent.soa.cfg;

import com.watent.soa.exception.SOAException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Reference标签的解析类
 *
 * @author Atom
 */

public class ReferenceBeanDifinitionParse implements BeanDefinitionParser {

    private Class<?> beanClass;

    public ReferenceBeanDifinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        //spring会把这个beanClass进行实例化  BeanDefinitionNames??
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");
        String intf = element.getAttribute("interface");
        String protocol = element.getAttribute("protocol");
        String loadbalance = element.getAttribute("loadbalance");
        String retries = element.getAttribute("retries");
        String cluster = element.getAttribute("cluster");

        if (StringUtils.isEmpty(id)) {
            throw new SOAException("Reference id could't be null！");
        }
        if (StringUtils.isEmpty(intf)) {
            throw new SOAException("Reference interface could't be null！");
        }
        if (StringUtils.isEmpty(protocol)) {
            throw new SOAException("Reference protocol could't be null！");
        }
        if (StringUtils.isEmpty(loadbalance)) {
            throw new SOAException("Reference loadbalance 不能为空！");
        }

        beanDefinition.getPropertyValues().addPropertyValue("id", id);
        beanDefinition.getPropertyValues().addPropertyValue("intf", intf);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("loadbalance", loadbalance);
        beanDefinition.getPropertyValues().addPropertyValue("retries", retries);
        beanDefinition.getPropertyValues().addPropertyValue("cluster", cluster);

        parserContext.getRegistry().registerBeanDefinition("Reference" + id,
                beanDefinition);


        return beanDefinition;
    }
}
