package org.dzirtbry.client;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dzirtbry.QueueNameBuilder;
import org.dzirtbry.annotations.CallableService;
import org.dzirtbry.sqs.QueueManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Component
public class SqsMethodInvocationHandler implements InvocationHandler {

    private final AmazonSQSClient sqsClient;
    private final QueueNameBuilder queueNameBuilder;
    private final ObjectMapper objectMapper;
    private final QueueManger queueManger;

    @Autowired
    public SqsMethodInvocationHandler(AmazonSQSClient sqsClient,
                                      QueueNameBuilder queueNameBuilder,
                                      ObjectMapper objectMapper,
                                      QueueManger queueManger) {
        this.sqsClient = sqsClient;
        this.queueNameBuilder = queueNameBuilder;
        this.objectMapper = objectMapper;
        this.queueManger = queueManger;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CallableService annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), CallableService.class);

        if (annotation == null) {
            return null;
        }

        String servicePrefix = annotation.name();
        String queueName = queueNameBuilder.getQueueName(servicePrefix, method);

        String queueUrl = queueManger.getQueueUrl(queueName, true);

        String messageBody = createMessageBody(args);

        sqsClient.sendMessage(queueUrl, messageBody);

        return null;
    }

    private String createMessageBody(Object[] args) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(args);
        return json;
    }
}
