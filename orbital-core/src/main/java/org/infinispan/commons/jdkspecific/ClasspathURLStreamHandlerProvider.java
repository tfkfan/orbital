package org.infinispan.commons.jdkspecific;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;

public class ClasspathURLStreamHandlerProvider extends URLStreamHandlerProvider {
    public ClasspathURLStreamHandlerProvider() {
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
        return "classpath".equals(protocol) ? new URLStreamHandler() {
            protected URLConnection openConnection(URL u) throws IOException {
                String path = u.getPath();
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                URL resource = classLoader == null ? null : classLoader.getResource(path);
                if (resource == null) {
                    resource = ClassLoader.getSystemClassLoader().getResource(path);
                }

                return resource != null ? resource.openConnection() : null;
            }
        } : null;
    }
}