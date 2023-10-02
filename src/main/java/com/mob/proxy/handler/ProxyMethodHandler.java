package com.mob.proxy.handler;

import java.lang.reflect.Method;

public interface ProxyMethodHandler {
    Object invoke(final Class<?> interfaceClass, final Method method, final Object[] args);
}
