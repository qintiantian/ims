package com.xylink;

import com.xylink.logic.MessageProcessor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by konglk on 2018/8/11.
 */
@SpringBootApplication
public class ImStarter {
    public static void main(String[] args) {
        SpringApplication.run(ImStarter.class);
    }

    @Autowired
    private MessageProcessor messageProcessor;

}
