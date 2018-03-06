package com.watent.soa.cfg;

import com.watent.soa.exception.SOAException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Registry 标签解析类
 *
 * @author Atom
 */
public class RegistryBeanDefinitionParse implements BeanDefinitionParser {


    private Class<?> beanClass;

    public RegistryBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        // beanDefinitionNames 实例化
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        String protocol = element.getAttribute("protocol");
        String address = element.getAttribute("address");

        if (StringUtils.isEmpty(protocol)) {
            throw new SOAException("protocol could't be null");
        }
        if (StringUtils.isEmpty(address)) {
            throw new SOAException("address could't be null");
        }

        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("address", address);

        parserContext.getRegistry().registerBeanDefinition("Registry" + address, beanDefinition);

        return beanDefinition;
    }
}
