package com.konglk.auth;

import com.konglk.conn.ClientConnection;
import com.konglk.conn.ClientConnectionMap;
import com.konglk.constants.ImsConstants;
import com.konglk.entity.UserVO;
import com.konglk.protobuf.Protocol;
import com.konglk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
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

    public UserVO login(Protocol.CLogin msg) {
        if(msg == null || StringUtils.isEmpty(msg.getUserId()) || StringUtils.isEmpty(msg.getPwd()))
            return null;
        UserVO userVO = userService.selectUser(msg.getUserId());
        if(userVO == null)
            return null;
        String encrptedpwd = DigestUtils.md5DigestAsHex((userVO.getSugar()+msg.getPwd()).getBytes());
        if(userVO.getPwd().equals(encrptedpwd))
            return userVO;
        return null;
    }

    public boolean isValidMsg(Protocol.CPrivateChat msg) {
        if(msg == null || StringUtils.isEmpty(msg.getDestId()))
            return false;
        return userService.selectUser(msg.getDestId())==null;
    }

    public boolean isValidUser(String userId, String certificate) {
        Map<String,Object> userVO = (Map<String, Object>) redisTemplate.opsForHash().get(ImsConstants.IMS_USERS, userId);
        if(userVO == null) {
            userVO = userService.selecUserById(userId);
            if(userVO == null) {
                return false;
            }
            redisTemplate.opsForHash().put(ImsConstants.IMS_USERS, userId, userVO);
        }
        ClientConnection c = ClientConnectionMap.getClientConnection(userId);
        if(c == null || StringUtils.isEmpty(c.getCertificate()))
            return false;
        return c.getCertificate().equals(certificate);
    }

}
