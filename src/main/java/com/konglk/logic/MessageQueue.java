package com.konglk.logic;

import com.konglk.auth.AuthService;
import com.konglk.cache.RedisManager;
import com.konglk.constants.ImsConstants;
import com.konglk.protobuf.Protocol;
import com.konglk.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by konglk on 2018/8/11.
 */
@Component
public class MessageQueue {

    @Autowired
    private RedisManager redisManager;

    public void push( Protocol.CPrivateChat msg) {
        redisManager.lpush(ImsConstants.IMS_MESSAGES, msg);
    }

    public void push2NotReadQueue(Protocol.CPrivateChat msg) {
        redisManager.lpush(ImsConstants.IMS_NOT_READ_MESSAGES+msg.getDestId(), msg);
    }

    public Protocol.CPrivateChat pop() {
        return redisManager.rpop(ImsConstants.IMS_MESSAGES);
    }

    public Protocol.CPrivateChat notReadPop(String userId) {
        return redisManager.rpop(ImsConstants.IMS_NOT_READ_MESSAGES+userId);
    }
}
