package com.xylink.auth;

import com.xylink.conn.ClientConnection;
import com.xylink.conn.ClientConnectionMap;
import com.xylink.entity.UserVO;
import com.xylink.protobuf.Protocol;
import com.xylink.service.UserService;
import com.xylink.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by konglk on 2018/8/13.
 */
@Service
public class AuthService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    public boolean login(Protocol.CLogin msg) {
        if(msg == null || StringUtils.isEmpty(msg.getUserId()) || StringUtils.isEmpty(msg.getPwd()))
            return false;
        UserVO userVO = userService.selectUser(msg.getUserId());
        if(userVO == null)
            return false;
        String encrptedpwd = EncryptUtils.crypt(userVO.getSugar()+msg.getPwd());
        return userVO.getPwd().equals(encrptedpwd);
    }

    public boolean isValidMsg(Protocol.CPrivateChat msg) {
        if(msg == null || StringUtils.isEmpty(msg.getDestId()))
            return false;
        return userService.selectUser(msg.getDestId())==null;
    }

    public boolean isValidUser(String userId, String certificate) {
        Map<String,Object> userVO = userService.selecUserById(userId);
        if(userVO == null) {
            return false;
        }
        ClientConnection c = ClientConnectionMap.getClientConnection(userId);
        if(c == null || StringUtils.isEmpty(c.getCertificate()))
            return false;
        return c.getCertificate().equals(certificate);
    }

}
