package org.dzirtbry.sqs;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
public interface QueueManger {

    String getQueueUrl(String queueName, boolean createIsMissing);
}
