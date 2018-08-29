package com.xylink.logic;

import com.xylink.auth.AuthService;
import com.xylink.conn.ClientConnection;
import com.xylink.conn.ClientConnectionMap;
import com.xylink.constants.ImsConstants;
import com.xylink.entity.UserVO;
import com.xylink.protobuf.Protocol;
import com.xylink.service.UserService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
