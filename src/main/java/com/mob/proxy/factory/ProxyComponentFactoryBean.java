package com.mob.proxy.factory;

import com.mob.proxy.annotation.ProxyComponent;
import com.mob.proxy.beandef.ProxyInvocationHandlerBeanNameGenerator;
import com.mob.proxy.handler.ProxyInvocationHandler;
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
    protected final ProxyInvocationHandlerBeanNameGenerator proxyInvocationHandlerBeanNameGenerator;
    private BeanFactory beanFactory;

    public ProxyComponentFactoryBean(
            final AnnotationMetadata metadata,
            final ProxyInvocationHandlerFactory proxyInvocationHandlerFactory,
            final ProxyInvocationHandlerBeanNameGenerator proxyInvocationHandlerBeanNameGenerator) {
        this.metadata = metadata;
        this.objectType = ClassUtils.resolveClassName(metadata.getClassName(), null);
        this.proxyInvocationHandlerFactory = proxyInvocationHandlerFactory;
        this.proxyInvocationHandlerBeanNameGenerator = proxyInvocationHandlerBeanNameGenerator;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getObject() {
        final Class<?> interfaceClass = ClassUtils.resolveClassName(metadata.getClassName(), null);
        final ProxyComponent proxyComponent = getProxyComponentAnnotation(interfaceClass);
        final String handlerName = proxyComponent.proxyMethodHandlerName();
        final ProxyInvocationHandler proxyInvocationHandler =
                getBeanFactory()
                        .getBean(
                                getProxyInvocationHandlerBeanName(handlerName),
                                ProxyInvocationHandler.class);

        return proxyInvocationHandlerFactory.createProxy(interfaceClass, proxyInvocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    protected BeanFactory getBeanFactory() {
        return beanFactory;
    }

    protected ProxyComponent getProxyComponentAnnotation(final Class<?> interfaceClass) {
        return interfaceClass.getAnnotation(ProxyComponent.class);
    }

    protected String getProxyInvocationHandlerBeanName(final String handlerBeanName) {
        return proxyInvocationHandlerBeanNameGenerator.generateBeanName(handlerBeanName);
    }
}
