package org.dzirtbry.sqs;


import com.amazonaws.services.sqs.model.Message;

import java.io.IOException;

/**
 * Parse received SQS message to build instance of type {@code T}
 *
 * @author volodymk
 */
public interface SqsMessageParser<T> {

    /**
     * Read the notification and parse it to build instance of T
     *
     * @param notification SQS message to parse
     * @return instance of T parsed from the message
     * @throws IOException if any exception occurred during parsing
     */
    T parse(Message notification) throws IOException;
}
