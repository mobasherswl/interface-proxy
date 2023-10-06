package com.mob.proxy.annotation;

import com.mob.proxy.InterfaceProxyConfiguration;
import com.mob.proxy.ProxyImportSelector;
import com.mob.proxy.beandef.ProxyComponentBeanDefinitionRegistrar;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ProxyImportSelector.class)
public @interface EnableInterfaceProxy {
    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};

    Class<?>[] configImports() default {
        InterfaceProxyConfiguration.class, ProxyComponentBeanDefinitionRegistrar.class
    };
}
