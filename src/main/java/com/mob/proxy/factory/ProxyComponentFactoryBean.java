package com.mob.proxy.factory;

import com.mob.proxy.annotation.ProxyComponent;
import com.mob.proxy.handler.ProxyMethodHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

public class ProxyComponentFactoryBean implements FactoryBean<Object>, BeanFactoryAware {
    protected final AnnotationMetadata metadata;
    protected final Class<?> objectType;
    protected final ProxyInvocationHandlerFactory proxyInvocationHandlerFactory;
    private BeanFactory beanFactory;

    public ProxyComponentFactoryBean(final AnnotationMetadata metadata,
                                     final ProxyInvocationHandlerFactory proxyInvocationHandlerFactory) {
        this.metadata = metadata;
        this.objectType = ClassUtils.resolveClassName(metadata.getClassName(), null);
        this.proxyInvocationHandlerFactory = proxyInvocationHandlerFactory;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getObject() {
        final Class<?> interfaceClass = ClassUtils.resolveClassName(metadata.getClassName(), null);
        final ProxyComponent proxyComponent = interfaceClass.getAnnotation(ProxyComponent.class);
        final String handlerBeanName = proxyComponent.handlerName();
        final ProxyMethodHandler proxyMethodHandler=  getBeanFactory().getBean(handlerBeanName, ProxyMethodHandler.class);

        return proxyInvocationHandlerFactory.createProxy(interfaceClass, proxyMethodHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    protected BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
