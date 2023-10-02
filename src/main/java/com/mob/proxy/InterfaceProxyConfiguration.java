package com.mob.proxy;

import com.mob.proxy.factory.ProxyInvocationHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfiguration {
    @Bean
    ProxyInvocationHandlerFactory proxyInvocationHandlerFactory() {
        return new ProxyInvocationHandlerFactory();
    }
}
