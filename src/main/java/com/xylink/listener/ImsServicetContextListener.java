package com.xylink.listener;

import com.xylink.gate.GateServer;
import com.xylink.utils.ExecutorUtils;
import com.xylink.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by konglk on 2018/8/29.
 */
@WebListener
public class ImsServicetContextListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("#########tomcat web application initialized##########");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("#######tomcat web application destroyed######");
        GateServer server = SpringUtils.getBean(GateServer.class);
        server.shutdown();
        logger.info("shutdown netty at port "+server.port);
        ExecutorUtils.executorService.shutdown();
    }
}
