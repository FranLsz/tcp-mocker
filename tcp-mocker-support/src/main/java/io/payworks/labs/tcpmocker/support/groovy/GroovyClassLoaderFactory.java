package io.payworks.labs.tcpmocker.support.groovy;

import groovy.lang.GroovyClassLoader;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class GroovyClassLoaderFactory {

    private GroovyClassLoaderFactory() {
    }

    public static GroovyClassLoader newGroovyClassLoaderViaPriviligedAction() {
        return AccessController.doPrivileged((PrivilegedAction<GroovyClassLoader>) GroovyClassLoader::new);
    }

    public static GroovyClassLoader newGroovyClassLoader() {
        return new GroovyClassLoader();
    }
}
