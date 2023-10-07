package com.mob.proxy;

import com.mob.proxy.annotation.ProxyComponent;
import com.mob.proxy.beandef.CustomAnnotationBeanNameGenerator;
import com.mob.proxy.beandef.ProxyComponentBeanDefinitionScanner;
import com.mob.proxy.beandef.ProxyInvocationHandlerBeanNameGenerator;
import com.mob.proxy.factory.ProxyComponentFactoryBean;
import com.mob.proxy.factory.ProxyInvocationHandlerFactory;
import com.mob.proxy.handler.DefaultProxyInvocationHandler;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Configuration
public class InterfaceProxyConfiguration {
    @Bean
    ProxyInvocationHandlerFactory proxyInvocationHandlerFactory() {
        return new ProxyInvocationHandlerFactory();
    }

    @Bean
    ProxyInvocationHandlerBeanNameGenerator proxyInvocationHandlerBeanNameGenerator() {
        return new ProxyInvocationHandlerBeanNameGenerator();
    }

    @Bean
    BeanNameGenerator customAnnotationBeanNameGenerator() {
        return new CustomAnnotationBeanNameGenerator(ProxyComponent.class);
    }

    @Bean
    ProxyComponentBeanDefinitionScanner proxyComponentBeanDefinitionScanner(
            final BeanDefinitionRegistry registry) {
        final ProxyComponentBeanDefinitionScanner bean =
                new ProxyComponentBeanDefinitionScanner(
                        registry,
                        proxyInvocationHandlerBeanNameGenerator(),
                        ProxyComponentFactoryBean.class,
                        DefaultProxyInvocationHandler.class);

        bean.addIncludeFilter(new AnnotationTypeFilter(ProxyComponent.class));
        bean.setBeanNameGenerator(customAnnotationBeanNameGenerator());

        return bean;
    }
}
