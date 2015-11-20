package org.dzirtbry.service;

import org.dzirtbry.annotations.CallableService;
import org.dzirtbry.annotations.Param;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@CallableService(name = "DummyService")
public interface DummyService {

    void foo();

    void bar(@Param("test") String test);

    void zuz(int a);

    void rar(int a, long b, String c);

    void cac(RequestObject requestObject);

}
