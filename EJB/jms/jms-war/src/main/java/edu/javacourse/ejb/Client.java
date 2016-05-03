package edu.javacourse.ejb;

import edu.javacourse.jms.SessionSenderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ResendServlet", urlPatterns = "/resendServlet")
public class Client extends HttpServlet {
    private Logger log = LoggerFactory.getLogger(Client.class);

    @EJB
    SessionSenderBean senderBean;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Servlet started!");

        final String destination = request.getParameter("dest");
        if ("queue".equals(destination)){
                senderBean.sendMessageToQueue();
        } else if ("topic".equals(destination)) {
            senderBean.sendMessageToTopic();
        } else {
            resp.getWriter().write("'dest' parameter need to be provided. queue/topic");
        }

        log.debug("Servlet finished!");
    }
}
