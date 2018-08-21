package com.xylink.logic;

import com.xylink.auth.AuthService;
import com.xylink.conn.ClientConnection;
import com.xylink.conn.ClientConnectionMap;
import com.xylink.protobuf.Protocol;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by konglk on 2018/8/11.
 */
@Component
public class MessageDispatcher {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AuthService authService;
    @Autowired
    private MessageQueue messageQueue;
    @Autowired
    private MessageProcessor messageProcessor;

    public void dispatch(ChannelHandlerContext ctx, Protocol.ProtocolMessage msg) {
        Protocol.ProtocolMessage.RequestType reqType = msg.getRequest().getReqType();
        ClientConnection c = ClientConnectionMap.getClientConnection(ctx.channel().attr(ClientConnection.netIdKey).get());
        if(c == null)
            return;
        switch (reqType) {
            case LOGIN:
                Protocol.CLogin m = msg.getRequest().getLogin();
                if(authService.login(m)) {
                    logger.info("user ["+m.getUserId()+"] login sucess");
                    ClientConnectionMap.buildSession(c, m);
                    messageProcessor.handleNotReadMessage(m.getUserId());
                }else{
                    logger.warn("user ["+m.getUserId()+"] invalid user or password");
                }
                break;
            case CHAT:
                if(StringUtils.isEmpty(c.getUserId()))
                    logger.warn("not login");
                else{
                    messageQueue.push(msg.getRequest().getChat());
                }
                break;
        }
    }

}
