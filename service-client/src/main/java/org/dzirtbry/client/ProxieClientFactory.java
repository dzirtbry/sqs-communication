package org.dzirtbry.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Component
public class ProxieClientFactory implements ClientFactory {

    private final InvocationHandler invocationHandler;

    @Autowired
    public ProxieClientFactory(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }


    @SuppressWarnings("unchecked")
    public <T> T getClient(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }
}
