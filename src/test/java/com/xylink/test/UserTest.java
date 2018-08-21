package com.xylink.test;

import com.xylink.ImStarter;
import com.xylink.config.BeanConfig;
import com.xylink.constants.ImsConstants;
import com.xylink.gate.GateServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konglk on 2018/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void insertUsers() {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> users = new HashMap<>();
        users.put("konglk", "konglk");
        users.put("qintian", "qintian");
        users.put("wumeng", "wumeng");
        hashOps.putAll(ImsConstants.IMS_USERS, users);

        System.out.println((hashOps.get(ImsConstants.IMS_USERS, "konglk")));
    }
}
