package org.dzirtbry.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Long polling consumer for SQS queue. If message was successfully processed by the listener - deletes message from the
 * queue.
 *
 * @author volodymk
 */
public class LongPollingSqsConsumer implements SqsConsumer {
    private static final Logger LOGGER = Logger.getLogger(LongPollingSqsConsumer.class.getName());
    private static final int LONG_POLL_TIME_SECONDS = 20;

    private final AmazonSQS client;
    private final String url;
    private final ScheduledExecutorService executor;

    private SqsListener sqsListener;
    private boolean running = false;

    public LongPollingSqsConsumer(AmazonSQS client, String url, SqsListener sqsListener) {
        this(client, url, sqsListener, Executors.newScheduledThreadPool(1));
    }

    LongPollingSqsConsumer(AmazonSQS client, String url, SqsListener sqsListener, ScheduledExecutorService executorService) {
        this.client = client;
        this.url = url;
        this.sqsListener = sqsListener;
        this.executor = executorService;
    }

    @Override
    public void start() {
        running = true;
        executor.execute(new ConsumeWrapper());
    }

    /**
     * Stop polling messages from SQS queue and dispatching to the listener. If long poll is in process - it will be
     * completed as usual.
     */
    @Override
    public void terminate() {
        running = false;
        executor.shutdown();
    }

    /**
     * Runnable wrapper around {@code consumeMessages}. If consumer is in running state - self-schedules after
     * completion.
     */
    class ConsumeWrapper implements Runnable {

        @Override
        public void run() {
            try {
                consumeMessages();
                if (running) {
                    executor.execute(this);
                }
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Failed to poll and dispatch messages", ex);
                if (running) {
                    // If there was an exception - delay the next poll 
                    executor.schedule(this, LONG_POLL_TIME_SECONDS, TimeUnit.SECONDS);
                }
            }
        }
    }

    /**
     * Receive messages from the queue and dispatch them one-by-one to the listener
     */
    void consumeMessages() {
        ReceiveMessageResult receiveMessageResult = longPollMessages();

        List<Message> messages = receiveMessageResult.getMessages();

        for (Message message : messages) {
            dispatch(message);
        }
    }

    /**
     * Process message to listener if one exists. If message if processed successfully - remove it from the queue.
     *
     * @param message SQS message
     */
    protected void dispatch(Message message) {
        try {
            boolean messageProcessed = false;
            if (sqsListener != null) {
                messageProcessed = sqsListener.onMessage(message);
            } else {
                LOGGER.info("Message " + message + "  is not dispatched because listener is null");
            }
            if (messageProcessed) {
                removeMessageFromQueue(message);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed dispatching message " + message, e);
        }
    }

    private void removeMessageFromQueue(Message message) {
        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();
        deleteMessageRequest.setQueueUrl(url);
        deleteMessageRequest.setReceiptHandle(message.getReceiptHandle());
        client.deleteMessage(deleteMessageRequest);
    }

    public SqsListener getSqsListener() {
        return sqsListener;
    }

    public void setSqsListener(SqsListener sqsListener) {
        this.sqsListener = sqsListener;
    }

    ExecutorService getExecutor() {
        return executor;
    }

    /**
     * If consumer if running.
     *
     * @return true if consumer is running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Polls messages for 30 seconds. If messages is available earlier - returns earlier. If no messages is available
     * after 30 seconds return empty result.
     */
    private ReceiveMessageResult longPollMessages() {
        ReceiveMessageRequest request = new ReceiveMessageRequest(url);
        request.setWaitTimeSeconds(LONG_POLL_TIME_SECONDS);
        request.setMaxNumberOfMessages(10);
        return client.receiveMessage(request);
    }

}
