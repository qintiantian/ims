package com.konglk.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * Created by konglk on 2018/8/31.
 */
@Service
public class ImsApplicationListener implements ApplicationListener<ContextClosedEvent> {
    private Logger logger= LoggerFactory.getLogger(ImsApplicationListener.class);
;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("############shutdown spring application##############");
        taskExecutor.shutdown();
    }
}
