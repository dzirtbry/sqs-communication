package org.dzirtbry.sqs;

import com.amazonaws.services.sqs.model.Message;

/**
 * SQS Message listener.
 *
 * @author volodymk
 */
public interface SqsListener {

    /**
     * Handle received SQS message
     *
     * @param message SQS message
     * @return true if message was processed successfully, false otherwise
     * @throws Exception exception occurred during message processing
     */
    public boolean onMessage(Message message) throws Exception;

}
