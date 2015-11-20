package org.dzirtbry.client;

import org.dzirtbry.service.DummyService;
import org.dzirtbry.service.RequestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 11/19/15.
 *
 * @author volodymk
 */
@Service
public class ServiceClientRunner {

    @Autowired
    private ClientFactory clientFactory;

    @PostConstruct
    public void run() {
        DummyService dummyService = clientFactory.getClient(DummyService.class);


//        dummyService.toString();
        dummyService.foo();
//        dummyService.foo();
//        dummyService.foo();
//
        dummyService.bar("test-test-test");

        dummyService.zuz(1010101);
//        dummyService.bar("blah-blah-blah");
//        dummyService.bar("I-don't-know-what-I'm doing");

        dummyService.rar(10, -10L, "stststs");


        RequestObject requestObject = new RequestObject();
        requestObject.setTest1("tetete");
        requestObject.setTest2(222);
        dummyService.cac(requestObject);

    }

}
