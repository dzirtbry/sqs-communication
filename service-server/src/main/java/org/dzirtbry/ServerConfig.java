package org.dzirtbry;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Configuration
@PropertySource("classpath:app.properties")
@ComponentScan("org.dzirtbry")
public class ServerConfig {

    @Autowired
    private Environment environment;

    @Bean
    public AmazonSQSClient sqsClient() {
        String accessKey = environment.getProperty("AMZN_ACCESS_KEY");
        String secretKey = environment.getProperty("AMZN_SECRET_KEY");
        AmazonSQSClient client = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey));
        return client;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
