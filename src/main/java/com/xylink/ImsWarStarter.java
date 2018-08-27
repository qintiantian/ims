package com.xylink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by konglk on 2018/8/27.
 */
@SpringBootApplication
public class ImsWarStarter extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ImsWarStarter.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ImsWarStarter.class, args);
    }
}
