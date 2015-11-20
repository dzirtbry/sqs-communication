package org.dzirtbry.sqs;

/**
 * SQS queue consumer.
 *
 * @author volodymk
 */
public interface SqsConsumer {

    /**
     * Start polling messages from the queue. If there is a listener registered message will be dispatched to the
     * listener.
     */
    void start();

    /**
     * Stop polling messages from SQS queue and dispatching to the listener.
     */
    void terminate();

    /**
     * Set the listener for the messages.
     *
     * @param sqsListener SQS message listener
     */
    void setSqsListener(SqsListener sqsListener);

}
