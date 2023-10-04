package com.mob.proxy.handler;

import java.lang.reflect.Method;

public class DefaultProxyInvocationHandler implements ProxyInvocationHandler {

    private final ProxyMethodHandler proxyMethodHandler;

    public DefaultProxyInvocationHandler(ProxyMethodHandler proxyMethodHandler) {
        this.proxyMethodHandler = proxyMethodHandler;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        final Class<?> clazz = getInterface(proxy);

        return proxyMethodHandler.invoke(new ProxyParam(clazz, method, args));
    }

    private Class<?> getInterface(final Object proxy) {
        return proxy.getClass().getInterfaces()[0];
    }
}
