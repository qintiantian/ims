package com.xylink.logic;

import com.google.protobuf.ByteString;
import com.xylink.auth.AuthService;
import com.xylink.cache.RedisManager;
import com.xylink.constants.ImsConstants;
import com.xylink.entity.MsgVO;
import com.xylink.entity.UserVO;
import com.xylink.enums.MsgConfig;
import com.xylink.protobuf.Protocol;
import com.xylink.service.MsgService;
import com.xylink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * Created by konglk on 2018/8/11.
 */
@Component
public class MessageQueue {
    @Autowired
    private AuthService authService;
    @Autowired
    private MsgService msgService;
    @Autowired
    private RedisManager redisManager;

    public void push( Protocol.CPrivateChat msg) {
        if(authService.isValidMsg(msg)){
            redisManager.lpush(ImsConstants.IMS_MESSAGES, msg);
            msgService.insertMsg(msgService.buildMsg(msg));
        }
    }

    public void push2NotReadQueue(Protocol.CPrivateChat msg) {
        if(authService.isValidMsg(msg)) {
            redisManager.lpush(ImsConstants.IMS_NOT_READ_MESSAGES+msg.getDestId(), msg);
        }
    }

    public Protocol.CPrivateChat pop() {
        return redisManager.rpop(ImsConstants.IMS_MESSAGES);
    }

    public Protocol.CPrivateChat notReadPop(String userId) {
        return redisManager.rpop(ImsConstants.IMS_NOT_READ_MESSAGES+userId);
    }
}
