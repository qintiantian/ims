package com.konglk.logic;

import com.konglk.conn.ClientConnection;
import com.konglk.conn.ClientConnectionMap;
import com.konglk.protobuf.Protocol;
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
    private LoginHandler loginHandler;
    @Autowired
    private ChatHandler chatHandler;


    public void dispatch(ChannelHandlerContext ctx, Protocol.ProtocolMessage msg) {
        Protocol.ProtocolMessage.RequestType reqType = msg.getRequest().getReqType();
        ClientConnection c = ClientConnectionMap.getClientConnection(ctx.channel().attr(ClientConnection.netIdKey).get());
        if (c == null)
            return;
        switch (reqType) {
            case LOGIN:
                loginHandler.process(ctx, msg);
                break;
            case CHAT:
                if (StringUtils.isEmpty(c.getUserId())) {
                    logger.warn("not login");
                }
                else {
                    chatHandler.process(ctx, msg);
                }
                break;
        }
    }

}
