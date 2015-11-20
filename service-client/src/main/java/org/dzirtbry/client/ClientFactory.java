package org.dzirtbry.client;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
public interface ClientFactory {

    <T> T getClient(Class<T> clazz);
}
