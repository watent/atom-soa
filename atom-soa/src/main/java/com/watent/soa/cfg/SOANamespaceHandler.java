package com.watent.soa.cfg;

import com.watent.soa.bean.Protocol;
import com.watent.soa.bean.Reference;
import com.watent.soa.bean.Registry;
import com.watent.soa.bean.Service;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 元素处理
 *
 * @author Atom
 */
public class SOANamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {

        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParse(Registry.class));
        registerBeanDefinitionParser("protocol", new ProtocolBeanDefinitionParse(Protocol.class));
        registerBeanDefinitionParser("reference", new ReferenceBeanDifinitionParse(Reference.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(Service.class));
    }
}
