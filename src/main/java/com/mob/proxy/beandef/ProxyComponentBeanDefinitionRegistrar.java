package com.mob.proxy.beandef;

import com.mob.proxy.annotation.EnableInterfaceProxy;
import com.mob.proxy.annotation.ProxyComponent;
import java.lang.annotation.Annotation;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotationMetadata;

public class ProxyComponentBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(
            final AnnotationMetadata configMetadata, final BeanDefinitionRegistry registry) {
        configMetadata.getAnnotations().stream(getAnnotationType())
                .forEach(
                        annotation -> {
                            String[] packageNames = getPackageNames(annotation);

                            getScanner(registry, getProxyComponentAnnotation()).scan(packageNames);
                        });
    }

    protected ClassPathBeanDefinitionScanner getScanner(
            final BeanDefinitionRegistry registry,
            final Class<? extends Annotation> markerAnnotation) {
        return new ProxyComponentBeanDefinitionScanner(registry, markerAnnotation);
    }

    protected Class<? extends Annotation> getProxyComponentAnnotation() {
        return ProxyComponent.class;
    }

    protected String[] getPackageNames(final MergedAnnotation<? extends Annotation> annotation) {
        return ((EnableInterfaceProxy) annotation.synthesize()).basePackages();
    }

    protected Class<? extends Annotation> getAnnotationType() {
        return EnableInterfaceProxy.class;
    }
}
