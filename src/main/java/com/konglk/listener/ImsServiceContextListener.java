package com.konglk.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by konglk on 2018/8/29.
 */
@WebListener
public class ImsServiceContextListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("#########tomcat web application initialized##########");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(("###########stop web application##########"));
    }
}
