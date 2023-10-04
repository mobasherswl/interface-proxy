package com.mob.proxy.beandef;

import com.mob.proxy.factory.ProxyComponentFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

public class ProxyComponentBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    public ProxyComponentBeanDefinitionScanner(
            final BeanDefinitionRegistry registry,
            final Class<? extends Annotation> annotationType
    ) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(annotationType));
        setBeanNameGenerator(new CustomAnnotationBeanNameGenerator(annotationType));
    }

    @Override
    protected boolean isCandidateComponent(final AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isInterface() && !metadata.isAnnotation();
    }

    @Override
    protected void postProcessBeanDefinition(final AbstractBeanDefinition beanDefinition, final String beanName) {
        beanDefinition.setBeanClassName(getFactoryBeanClassName());

        beanDefinition
                .getConstructorArgumentValues()
                .addGenericArgumentValue(((AnnotatedBeanDefinition) beanDefinition).getMetadata());
    }

    protected String getFactoryBeanClassName() {
        return ProxyComponentFactoryBean.class.getName();
    }
}