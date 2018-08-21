package com.xylink.gate;

import com.xylink.protobuf.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by konglk on 2018/8/13.
 */
public class GateAuthConnectionHandler extends SimpleChannelInboundHandler<Protocol.ProtocolMessage> {
    private static ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        GateAuthConnectionHandler.ctx = ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol.ProtocolMessage msg) throws Exception {

    }

    public static ChannelHandlerContext getCtx() {
        return ctx;
    }
}
