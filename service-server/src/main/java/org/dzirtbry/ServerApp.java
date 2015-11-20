package org.dzirtbry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class ServerApp
{
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ServerConfig.class);
    }
}
