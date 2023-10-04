package com.mob.proxy.beandef;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

public class CustomAnnotationBeanNameGenerator extends AnnotationBeanNameGenerator {
    private final Class<? extends Annotation> annotationType;

    public CustomAnnotationBeanNameGenerator(final Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    protected boolean isStereotypeWithNameValue(
            final String annotationType,
            final Set<String> metaAnnotationTypes,
            final Map<String, Object> attributes) {

        boolean isStereotype = annotationType.equals(this.annotationType.getClass().getName());

        return (isStereotype && attributes != null && attributes.containsKey("value"));
    }
}
