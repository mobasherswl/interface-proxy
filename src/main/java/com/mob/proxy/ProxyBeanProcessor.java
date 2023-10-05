package com.walmart.store.lnmgnt.bpo.wf.task.proxy;

import com.mob.proxy.AnnotatedInterfaceScanner;
import com.mob.proxy.ProxyInvocationHandler;
import java.beans.Introspector;
import java.lang.reflect.Proxy;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class ProxyBeanProcessor {

    private final ProxyInvocationHandler proxyInvocationHandler;
    private final AnnotatedInterfaceScanner annotatedInterfaceScanner;
    private final ConfigurableListableBeanFactory beanFactory;

    public ProxyBeanProcessor(
            ProxyInvocationHandler proxyInvocationHandler,
            AnnotatedInterfaceScanner annotatedInterfaceScanner,
            ConfigurableListableBeanFactory beanFactory) {
        this.proxyInvocationHandler = proxyInvocationHandler;
        this.annotatedInterfaceScanner = annotatedInterfaceScanner;
        this.beanFactory = beanFactory;
    }

    public void process() {
        annotatedInterfaceScanner
                .scan()
                .forEach(
                        clazz -> {
                            final Object proxy =
                                    Proxy.newProxyInstance(
                                            proxyInvocationHandler.getClass().getClassLoader(),
                                            new Class<?>[] {clazz},
                                            proxyInvocationHandler);
                            final String beanName =
                                    Introspector.decapitalize(clazz.getSimpleName());

                            beanFactory.registerSingleton(beanName, proxy);
                        });
    }
}
