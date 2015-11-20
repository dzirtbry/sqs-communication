package org.dzirtbry.server;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dzirtbry.sqs.QueueNameBuilder;
import org.dzirtbry.annotations.CallableService;
import org.dzirtbry.sqs.LongPollingSqsConsumer;
import org.dzirtbry.sqs.QueueManger;
import org.dzirtbry.sqs.SqsConsumer;
import org.dzirtbry.sqs.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Service
public class SqsServiceManager implements ServiceManager {

    private final AmazonSQS sqsClient;
    private final QueueNameBuilder queueNameBuilder;
    private final QueueManger queueManger;
    private final ObjectMapper objectMapper;

    private Map<String, SqsConsumer> consumerMap = new HashMap<String, SqsConsumer>();

    @Autowired
    public SqsServiceManager(AmazonSQS sqsClient,
                             QueueNameBuilder queueNameBuilder,
                             QueueManger queueManger,
                             ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.queueNameBuilder = queueNameBuilder;
        this.queueManger = queueManger;
        this.objectMapper = objectMapper;
    }

    public boolean register(Object service) {
        Class<?> serviceClass = service.getClass();


        CallableService annotation = AnnotationUtils.findAnnotation(serviceClass, CallableService.class);
        if (annotation == null) {
            return false;
        }
        String servicePrefix = annotation.name();

        Class serviceInterface = serviceClass;
        Class<?>[] interfaces = serviceClass.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            if (anInterface.getAnnotation(CallableService.class) != null) {
                serviceInterface = anInterface;
                break;
            }
        }
        Method[] methods = serviceInterface.getMethods();

        for (Method method : methods) {
            String queueName = queueNameBuilder.getQueueName(servicePrefix, method);
            String queueUrl = queueManger.getQueueUrl(queueName, true);

            MethodListener methodListener = new MethodListener(service, method);
            LongPollingSqsConsumer consumer = new LongPollingSqsConsumer(sqsClient, queueUrl, methodListener);

            consumerMap.put(queueUrl, consumer);
            consumer.start();
        }

        return true;
    }

    class MethodListener implements SqsListener {
        private Object service;
        private Method method;

        public MethodListener(Object service, Method method) {
            this.service = service;
            this.method = method;
        }

        public boolean onMessage(Message message) throws Exception {
            String body = message.getBody();
            String[] strings = objectMapper.readValue(body, String[].class);

            Class[] parameterTypes = method.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i].equals(String.class)) {
                    parameters[i] = strings[i];
                } else {
                    parameters[i] = objectMapper.readValue(strings[i], parameterTypes[i]);
                }
            }

            method.invoke(service, parameters);
            return true;
        }
    }
}
