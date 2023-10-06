package com.mob.proxy.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ProxyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(final AnnotationMetadata importingClassMetadata) {
        final Class<?>[] imports =
                importingClassMetadata
                        .getAnnotations()
                        .get(EnableInterfaceProxy.class)
                        .synthesize()
                        .configImports();
        final String[] classNames = new String[imports.length];

        for (int i = 0; i < imports.length; i++) {
            classNames[i] = imports[i].getName();
        }

        return classNames;
    }
}
