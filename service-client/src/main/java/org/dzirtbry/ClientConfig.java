package org.dzirtbry;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Configuration
@Import(SqsConfig.class)
@ComponentScan("org.dzirtbry")
public class ClientConfig {
}
