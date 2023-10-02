package com.mob.proxy.factory;

import com.mob.proxy.handler.DefaultProxyInvocationHandler;
import com.mob.proxy.handler.ProxyInvocationHandler;
import com.mob.proxy.handler.ProxyMethodHandler;

import java.lang.reflect.Proxy;

public class ProxyInvocationHandlerFactory {

    public Object createProxy(final Class<?> interfaceClass,
                                 final ProxyMethodHandler proxyMethodHandler) {
        return Proxy.newProxyInstance(proxyMethodHandler.getClass().getClassLoader(),
                new Class<?>[] {interfaceClass},
                getProxyInvocationHandler(proxyMethodHandler));
    }

    protected ProxyInvocationHandler getProxyInvocationHandler(final ProxyMethodHandler proxyMethodHandler) {
        return new DefaultProxyInvocationHandler(proxyMethodHandler);
    }

}
