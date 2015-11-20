package org.dzirtbry;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Component
public class SimpleQueueNameBuilder implements QueueNameBuilder {
    private static final String SEPARATOR = "-";

    public String getQueueName(String servicePrefix, Method method) {
        return servicePrefix + SEPARATOR + method.getName();
    }

}
