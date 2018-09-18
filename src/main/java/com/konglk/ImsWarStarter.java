package com.konglk;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.konglk.gate.GateServer;
import com.konglk.webrtc.WebRTCServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * Created by konglk on 2018/8/27.
 */
@SpringBootApplication
@Import({FdfsClientConfig.class})
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class ImsWarStarter extends SpringBootServletInitializer implements CommandLineRunner {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ImsWarStarter.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ImsWarStarter.class, args);
    }

    @Autowired
    private GateServer gateServer;

    @Autowired
    private WebRTCServer webRTCServer;

    @Override
    public void run(String... args) throws Exception {
        gateServer.start();
//        webRTCServer.start();
    }
}
