package com.watent.soa.cfg;

import com.watent.soa.exception.SOAException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * service 标签解析
 *
 * @author Atom
 */
public class ServiceBeanDefinitionParse implements BeanDefinitionParser {

    private Class<?> beanClass;

    public ServiceBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        //spring会把这个beanClass进行实例化  BeanDefinitionNames??
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String intf = element.getAttribute("interface");
        String ref = element.getAttribute("ref");
        String protocol = element.getAttribute("protocol");

        if (StringUtils.isEmpty(intf)) {
            throw new SOAException("service intf could't be null！");
        }
        if (StringUtils.isEmpty(ref)) {
            throw new SOAException("service ref could't be null！");
        }
        if (StringUtils.isEmpty(protocol)) {
            throw new SOAException("service protocol could't be null！");
        }

        beanDefinition.getPropertyValues().addPropertyValue("intf", intf);
        beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);

        parserContext.getRegistry().registerBeanDefinition("service" + ref + intf, beanDefinition);
        
        return beanDefinition;
    }
}
