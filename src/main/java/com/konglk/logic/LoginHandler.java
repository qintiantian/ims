package com.konglk.logic;

import com.konglk.auth.AuthService;
import com.konglk.conn.ClientConnection;
import com.konglk.conn.ClientConnectionMap;
import com.konglk.constants.ImsConstants;
import com.konglk.entity.UserVO;
import com.konglk.protobuf.Protocol;
import com.konglk.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Created by konglk on 2018/8/25.
 */
@Component
public class LoginHandler implements IMessageHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageProcessor messageProcessor;
    @Override
    public void process(ChannelHandlerContext ctx, Protocol.ProtocolMessage message) {
        ClientConnection c = ClientConnectionMap.getClientConnection(ctx.channel().attr(ClientConnection.netIdKey).get());
        Protocol.CLogin m = message.getRequest().getLogin();
        m = decode(m);
        Protocol.ProtocolMessage.Builder msgBuilder = Protocol.ProtocolMessage.newBuilder();
        Protocol.ProtocolMessage.TResponse.Builder responseBuilder = Protocol.ProtocolMessage.TResponse.newBuilder();
        responseBuilder.setRespType(Protocol.ProtocolMessage.RequestType.LOGIN);
        Protocol.SResponse.Builder sResponseBuilder = Protocol.SResponse.newBuilder();
        UserVO userVO = authService.login(m);
        boolean loginSucess = false;
        if (userVO != null) {
            logger.info("user [" + m.getUserId() + "] login sucess");
            ClientConnectionMap.buildSession(c, userVO.getUserId());
            sResponseBuilder.setCode(ImsConstants.SUCCESS_CODE);
            sResponseBuilder.setCertificate(c.getCertificate());
            sResponseBuilder.setUserId(userVO.getUserId());
            loginSucess = true;
        } else {
            sResponseBuilder.setCode(ImsConstants.FAIL_CODE);
            logger.warn("user [" + m.getUserId() + "] invalid user or password");
        }
        responseBuilder.setResp(sResponseBuilder.build());
        msgBuilder.setResponse(responseBuilder.build());
        Protocol.ProtocolMessage responseMessage = msgBuilder.build();
        ctx.writeAndFlush(responseMessage);
        if(loginSucess) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            messageProcessor.handleNotReadMessage(m.getUserId());
        }
    }

    private Protocol.CLogin decode(Protocol.CLogin m){
        String userId = m.getUserId();
        String pwd = m.getPwd();
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(pwd))
            throw new IllegalArgumentException("error argument");
        String s1 = "konglingkai";
        String s2 = "qintiantian";
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            String t1 = new String(decoder.decode(userId),"utf8");
            String t2 = new String(decoder.decode(pwd), "utf8");
            if(t1.startsWith(s1))
                userId = t1.substring(s1.length());
            if(t2.startsWith(s2))
                pwd = t2.substring(s2.length());
            return Protocol.CLogin.newBuilder().mergeFrom(m).setUserId(userId).setPwd(pwd).build();
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return m;
    }
}
