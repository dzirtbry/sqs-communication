package org.dzirtbry.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    String value();
}
