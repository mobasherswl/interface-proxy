package com.mob.proxy.factory;

import com.mob.proxy.handler.ProxyInvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyInvocationHandlerFactory {

    public Object createProxy(
            final Class<?> interfaceClass, final ProxyInvocationHandler proxyInvocationHandler) {
        return Proxy.newProxyInstance(
                proxyInvocationHandler.getClass().getClassLoader(),
                new Class<?>[] {interfaceClass},
                proxyInvocationHandler);
    }
}
