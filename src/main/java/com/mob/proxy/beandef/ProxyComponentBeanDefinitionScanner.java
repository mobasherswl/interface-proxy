package com.mob.proxy.beandef;

import com.mob.proxy.annotation.ProxyComponent;
import com.mob.proxy.handler.ProxyInvocationHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;

public class ProxyComponentBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    private final ProxyInvocationHandlerBeanNameGenerator proxyInvocationHandlerBeanNameGenerator;
    private final Class<? extends FactoryBean> proxyComponentFactoryBeanClass;
    private final Class<? extends ProxyInvocationHandler> proxyInvocationHandlerClass;

    public ProxyComponentBeanDefinitionScanner(
            final BeanDefinitionRegistry registry,
            final ProxyInvocationHandlerBeanNameGenerator proxyInvocationHandlerBeanNameGenerator,
            final Class<? extends FactoryBean> proxyComponentFactoryBeanClass,
            final Class<? extends ProxyInvocationHandler> proxyInvocationHandlerClass) {
        super(registry, false);
        this.proxyInvocationHandlerBeanNameGenerator = proxyInvocationHandlerBeanNameGenerator;
        this.proxyComponentFactoryBeanClass = proxyComponentFactoryBeanClass;
        this.proxyInvocationHandlerClass = proxyInvocationHandlerClass;
    }

    @Override
    protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
        final AnnotationMetadata metadata = beanDefinition.getMetadata();

        return metadata.isInterface() && !metadata.isAnnotation();
    }

    @Override
    protected void postProcessBeanDefinition(
            final AbstractBeanDefinition beanDefinition, final String beanName) {
        beanDefinition.setBeanClassName(proxyComponentFactoryBeanClass.getName());
        beanDefinition
                .getConstructorArgumentValues()
                .addGenericArgumentValue(((AnnotatedBeanDefinition) beanDefinition).getMetadata());
    }

    @Override
    protected void registerBeanDefinition(
            final BeanDefinitionHolder definitionHolder, final BeanDefinitionRegistry registry) {
        super.registerBeanDefinition(definitionHolder, registry);

        registerProxyInvocationHandlerBeanDefinition(definitionHolder, registry);
    }

    protected void registerProxyInvocationHandlerBeanDefinition(
            final BeanDefinitionHolder definitionHolder, final BeanDefinitionRegistry registry) {
        final AnnotatedBeanDefinition abd =
                (AnnotatedBeanDefinition) definitionHolder.getBeanDefinition();
        final String proxyMethodHandlerName = getProxyMethodHandlerName(abd);
        final String beanName =
                proxyInvocationHandlerBeanNameGenerator.generateBeanName(proxyMethodHandlerName);

        if (!registry.containsBeanDefinition(beanName)) {
            final BeanDefinition beanDefinition =
                    BeanDefinitionBuilder.genericBeanDefinition(proxyInvocationHandlerClass)
                            .addConstructorArgReference(proxyMethodHandlerName)
                            .getBeanDefinition();

            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    protected String getProxyMethodHandlerName(
            final AnnotatedBeanDefinition annotatedBeanDefinition) {
        return annotatedBeanDefinition
                .getMetadata()
                .getAnnotations()
                .get(ProxyComponent.class)
                .synthesize()
                .proxyMethodHandlerName();
    }
}
