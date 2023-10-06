package com.mob.proxy.beandef;

import com.mob.proxy.annotation.EnableInterfaceProxy;
import com.mob.proxy.annotation.ProxyComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

public class ProxyComponentBeanDefinitionRegistrar
        implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(
            final AnnotationMetadata configMetadata, final BeanDefinitionRegistry registry) {
        configMetadata.getAnnotations().stream(getFeatureEnableAnnotationType())
                .forEach(
                        annotation -> {
                            final String[] packageNames = getPackageNames(annotation);

                            getScanner(registry, getProxyComponentAnnotationType())
                                    .scan(packageNames);
                        });
    }

    protected ClassPathBeanDefinitionScanner getScanner(
            final BeanDefinitionRegistry registry,
            final Class<ProxyComponent> proxyComponentAnnotationType) {
        return new ProxyComponentBeanDefinitionScanner(
                registry,
                proxyComponentAnnotationType,
                beanFactory.getBean(ProxyInvocationHandlerBeanNameGenerator.class));
    }

    protected Class<ProxyComponent> getProxyComponentAnnotationType() {
        return ProxyComponent.class;
    }

    protected String[] getPackageNames(final MergedAnnotation<EnableInterfaceProxy> annotation) {
        return annotation.synthesize().basePackages();
    }

    protected Class<EnableInterfaceProxy> getFeatureEnableAnnotationType() {
        return EnableInterfaceProxy.class;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
