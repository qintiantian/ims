package com.xylink.gate;

import com.xylink.conn.ClientConnectionMap;
import com.xylink.logic.MessageDispatcher;
import com.xylink.protobuf.Protocol;
import com.xylink.utils.SpringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by konglk on 2018/8/11.
 */
public class GateServerHandler extends SimpleChannelInboundHandler<Protocol.ProtocolMessage> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MessageDispatcher messageDispatcher = SpringUtils.getBean(MessageDispatcher.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("active a new channel " + ctx.channel().remoteAddress());
        ClientConnectionMap.addClientConnection(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("remove a channel " + ctx.channel().remoteAddress());
        ClientConnectionMap.removeClientConnection(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol.ProtocolMessage msg) throws Exception {
        messageDispatcher.dispatch(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
