package edu.javacourse.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;

@LocalBean
@Stateless(name = "SessionSenderEJB")
public class SessionSenderBean {
    public SessionSenderBean() {
    }

    private Logger log = LoggerFactory.getLogger(SessionSenderBean.class);

//    @Resource(mappedName = "java:/ConnectionFactory")
//    private ConnectionFactory connectionFactory;

    @Inject
//    @JMSConnectionFactory("java:/ConnectionFactory") // default value
            JMSContext jmsContext;

    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    @Resource(mappedName = "java:/topic/test")
    private Topic topic;

    public void sendMessageToQueue() {
        try {
//            Connection connection = connectionFactory.createConnection();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            MessageProducer messageProducer = session.createProducer(queue);
            assert jmsContext != null;
            JMSProducer messageProducer = jmsContext.createProducer();
            TextMessage message = jmsContext.createTextMessage();
            for (int i = 0; i < 20; i++) {
                message.setText("To queue message " + (i + 1) + ". " + System.currentTimeMillis());
                log.debug("Sending queue message: " + message.getText());
                log.warn(jmsContext.getClass().getName());
                messageProducer.send(queue, message);
                log.debug("queue message sent");
            }
//            messageProducer.close();
//            session.close();
//            connection.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
            log.debug(ex.getMessage());
        }
    }

    public void sendMessageToTopic() {
//        try {
//            Connection connection = connectionFactory.createConnection();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            eProducer messageProducer = session.createProducer(topic);
//            TextMessage message = session.createTextMessage();
        for (int i = 0; i < 10; i++) {
            String message = "Topic message " + (i + 1) + ". " + System.currentTimeMillis();
            log.debug("Sending Topic message: " + message);
            jmsContext.createProducer().send(queue, message);
            log.debug("Topic message sent");
        }
//            messageProducer.close();
//            session.close();
//            connection.close();
//        } catch (JMSException ex) {
//            ex.printStackTrace();
//            log.debug(ex.getMessage());
//        }
    }

}
