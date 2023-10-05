package com.mob.proxy;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

public class AnnotatedInterfaceScanner {
    private final String basePackage;
    private final Class<? extends Annotation> interfaceClass;

    public AnnotatedInterfaceScanner(
            String basePackage, Class<? extends Annotation> interfaceClass) {
        this.basePackage = basePackage;
        this.interfaceClass = interfaceClass;
    }

    public List<Class<?>> scan() {
        final ClassPathScanningCandidateComponentProvider provider =
                getAnnotatedInterfaceScanningProvider(interfaceClass);
        final Set<BeanDefinition> beanDefs = provider.findCandidateComponents(basePackage);

        return beanDefs.stream()
                .filter(beanDefinition -> beanDefinition instanceof AnnotatedBeanDefinition)
                .map(
                        beanDefinition ->
                                ClassUtils.resolveClassName(
                                        beanDefinition.getBeanClassName(), null))
                .collect(Collectors.toList());
    }

    private ClassPathScanningCandidateComponentProvider getAnnotatedInterfaceScanningProvider(
            final Class<? extends Annotation> clazz) {
        final ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false) {
                    @Override
                    protected boolean isCandidateComponent(
                            final AnnotatedBeanDefinition beanDefinition) {
                        final AnnotationMetadata metadata = beanDefinition.getMetadata();

                        return metadata.isInterface() && !metadata.isAnnotation();
                    }
                };

        provider.addIncludeFilter(new AnnotationTypeFilter(clazz));

        return provider;
    }
}
