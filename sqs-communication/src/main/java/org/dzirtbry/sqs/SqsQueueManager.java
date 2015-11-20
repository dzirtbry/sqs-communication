package org.dzirtbry.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Component
public class SqsQueueManager implements QueueManger {

    private final AmazonSQS client;

    @Autowired
    public SqsQueueManager(AmazonSQS client) {
        this.client = client;
    }

    public String getQueueUrl(String queueName, boolean createIfMissing) {
        try {
            GetQueueUrlResult getQueueUrlResult = client.getQueueUrl(queueName);
            return getQueueUrlResult.getQueueUrl();
        } catch (QueueDoesNotExistException e) {
            if (createIfMissing) {
                return createQueue(queueName);
            }
        }
        return null;
    }

    public String createQueue(String queueName) {
        CreateQueueResult queue = client.createQueue(queueName);
        return queue.getQueueUrl();
    }
}
