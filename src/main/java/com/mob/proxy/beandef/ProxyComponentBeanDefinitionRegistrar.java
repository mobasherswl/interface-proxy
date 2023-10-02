package com.mob.proxy.beandef;

import com.mob.proxy.annotation.EnableInterfaceProxy;
import com.mob.proxy.annotation.ProxyComponent;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

public class ProxyComponentBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(final AnnotationMetadata configMetadata,
                                        final BeanDefinitionRegistry registry) {
        configMetadata
                .getAnnotations()
                .stream(getAnnotationType())
                .forEach(annotation -> {
                    String[] packageNames = annotation.synthesize().basePackages();

                    getScanner(registry, ProxyComponent.class).scan(packageNames);
                });
    }

    private Class<EnableInterfaceProxy> getAnnotationType() {
        return EnableInterfaceProxy.class;
    }

    private ClassPathBeanDefinitionScanner getScanner(final BeanDefinitionRegistry registry,
                                                      final Class<? extends Annotation> markerAnnotation) {
        return new ProxyComponentBeanDefinitionScanner(registry, markerAnnotation);
    }
}
