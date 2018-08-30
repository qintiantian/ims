package com.konglk;

import com.konglk.listener.ImsServicetContextListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by konglk on 2018/8/27.
 */
@SpringBootApplication
public class ImsWarStarter extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ImsWarStarter.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(new ImsServicetContextListener());
        super.onStartup(servletContext);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ImsWarStarter.class, args);
    }
}
