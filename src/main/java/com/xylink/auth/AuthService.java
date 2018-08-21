package com.xylink.auth;

import com.xylink.constants.ImsConstants;
import com.xylink.protobuf.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konglk on 2018/8/13.
 */
@Service
public class AuthService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static Map<String, String> users = new HashMap<>();
    {
        users.put("konglk", "konglk");
        users.put("qintian", "qintian");
    }

    public boolean login(Protocol.CLogin msg) {
        if(msg == null || StringUtils.isEmpty(msg.getUserId()) || StringUtils.isEmpty(msg.getPwd()))
            return false;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String pwd = hashOps.get(ImsConstants.IMS_USERS, msg.getUserId());
        return msg.getPwd().equals(pwd);
    }

    public boolean isValidMsg(Protocol.CPrivateChat msg) {
        if(msg == null || StringUtils.isEmpty(msg.getDestId()))
            return false;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.hasKey(ImsConstants.IMS_USERS, msg.getDestId());
    }

}
