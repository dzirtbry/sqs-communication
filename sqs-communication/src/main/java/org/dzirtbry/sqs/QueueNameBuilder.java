package org.dzirtbry.sqs;

import java.lang.reflect.Method;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
public interface QueueNameBuilder {

    String getQueueName(String servicePrefix, Method method);

}
