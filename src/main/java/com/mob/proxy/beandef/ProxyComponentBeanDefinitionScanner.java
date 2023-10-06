package com.mob.proxy.beandef;

import com.mob.proxy.annotation.ProxyComponent;
import com.mob.proxy.factory.ProxyComponentFactoryBean;
import com.mob.proxy.handler.DefaultProxyInvocationHandler;
import com.mob.proxy.handler.ProxyInvocationHandler;
import java.lang.annotation.Annotation;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ProxyComponentBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    private final ProxyInvocationHandlerBeanNameGenerator proxyInvocationHandlerBeanNameGenerator;

    public ProxyComponentBeanDefinitionScanner(
            final BeanDefinitionRegistry registry,
            final Class<? extends Annotation> proxyComponentAnnotationType,
            final ProxyInvocationHandlerBeanNameGenerator proxyInvocationHandlerBeanNameGenerator) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(proxyComponentAnnotationType));
        setBeanNameGenerator(getBeanNameGenerator(proxyComponentAnnotationType));
        this.proxyInvocationHandlerBeanNameGenerator = proxyInvocationHandlerBeanNameGenerator;
    }

    @Override
    protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
        final AnnotationMetadata metadata = beanDefinition.getMetadata();

        return metadata.isInterface() && !metadata.isAnnotation();
    }

    @Override
    protected void postProcessBeanDefinition(
            final AbstractBeanDefinition beanDefinition, final String beanName) {
        beanDefinition.setBeanClassName(getProxyComponentFactoryBeanType().getName());
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
        final String methodHandlerName = getMethodHandlerName(abd);
        final String beanName =
                proxyInvocationHandlerBeanNameGenerator.generateBeanName(methodHandlerName);

        if (!registry.containsBeanDefinition(beanName)) {
            final BeanDefinition beanDefinition =
                    BeanDefinitionBuilder.genericBeanDefinition(getProxyInvocationHandlerType())
                            .addConstructorArgReference(methodHandlerName)
                            .getBeanDefinition();

            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    protected Class<? extends FactoryBean> getProxyComponentFactoryBeanType() {
        return ProxyComponentFactoryBean.class;
    }

    protected Class<? extends ProxyInvocationHandler> getProxyInvocationHandlerType() {
        return DefaultProxyInvocationHandler.class;
    }

    protected BeanNameGenerator getBeanNameGenerator(
            final Class<? extends Annotation> proxyComponentAnnotation) {
        return new CustomAnnotationBeanNameGenerator(proxyComponentAnnotation);
    }

    protected String getMethodHandlerName(final AnnotatedBeanDefinition annotatedBeanDefinition) {
        return annotatedBeanDefinition
                .getMetadata()
                .getAnnotations()
                .get(ProxyComponent.class)
                .synthesize()
                .proxyMethodHandlerName();
    }
}
