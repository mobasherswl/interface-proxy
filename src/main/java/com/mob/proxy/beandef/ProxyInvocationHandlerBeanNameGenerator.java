package com.mob.proxy.beandef;

public class ProxyInvocationHandlerBeanNameGenerator {
    public String generateBeanName(final String proxyMethodHandlerBeanName) {
        return proxyMethodHandlerBeanName + "InvocationHandler";
    }
}
