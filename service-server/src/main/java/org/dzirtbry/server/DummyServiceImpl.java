package org.dzirtbry.server;

import org.dzirtbry.service.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Service
public class DummyServiceImpl implements DummyService {
    private static final Logger LOGGER = Logger.getLogger(DummyServiceImpl.class.getName());

    private final ServiceManager serviceManager;

    @Autowired
    public DummyServiceImpl(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @PostConstruct
    public void init() {
        serviceManager.register(this);
    }

    public void foo() {
        System.out.println("FOOOOOOOOO");
    }

    public void bar(String test) {
        System.out.println("BAAAAARRRRR = " + test);
    }

    public void zuz(int a) {
        System.out.println(a);
    }

    public void rar(int a, long b, String c) {
        System.out.println(a + " - " + b + " - " + c);
    }
}
