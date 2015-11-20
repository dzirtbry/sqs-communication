package org.dzirtbry.sqs;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author volodymk
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface SqsEndpointUrl {
}
