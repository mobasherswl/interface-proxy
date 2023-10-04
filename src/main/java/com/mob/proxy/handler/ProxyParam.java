package com.mob.proxy.handler;

import java.lang.reflect.Method;

public class ProxyParam {
    private final Class<?> targetClass;
    private final Method method;
    private final Object[] arguments;

    public ProxyParam(final Class<?> targetClass, final Method method, final Object[] arguments) {
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
