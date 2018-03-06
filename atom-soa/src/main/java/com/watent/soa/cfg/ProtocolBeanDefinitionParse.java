package com.watent.soa.cfg;

import com.watent.soa.exception.SOAException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * 协议标签解析
 *
 * @author Atom
 */
public class ProtocolBeanDefinitionParse implements BeanDefinitionParser {

    private Class<?> beanClass;

    public ProtocolBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        // beanDefinitionNames 实例化
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        String name = element.getAttribute("name");
        String host = element.getAttribute("host");
        String port = element.getAttribute("port");
        String contextpath = element.getAttribute("contextpath");

        if (StringUtils.isEmpty(name)) {
            throw new SOAException("name could't be null");
        }
        if (StringUtils.isEmpty(host)) {
            throw new SOAException("host could't be null");
        }
        if (StringUtils.isEmpty(port)) {
            throw new SOAException("port could't be null");
        }

        beanDefinition.getPropertyValues().addPropertyValue("name", name);
        beanDefinition.getPropertyValues().addPropertyValue("host", host);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("contextpath", contextpath);
        parserContext.getRegistry().registerBeanDefinition("protocol" + host + port, beanDefinition);

        return beanDefinition;
    }
}
