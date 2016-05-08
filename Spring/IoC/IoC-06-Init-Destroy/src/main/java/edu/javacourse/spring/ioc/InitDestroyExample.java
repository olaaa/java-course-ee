package edu.javacourse.spring.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Artem Pronchakov | email/xmpp: artem.pronchakov@calisto.email
 */
public class InitDestroyExample {

    private static Logger log = LoggerFactory.getLogger(InitDestroyExample.class);

    public static void main(String[] args) throws InterruptedException {
//        ClassPathXmlApplicationContext уточняем тип
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"springContext.xml"});

        log.debug("\n\n\n");

        Thread.sleep(5000);

        log.debug("Destroying context...");
//        объект будут жить дальше, просто у них вызовется метод destroy
        context.destroy();
    }

}
