package com.xylink.auth;

import com.xylink.conn.ClientConnection;
import com.xylink.conn.ClientConnectionMap;
import com.xylink.protobuf.Protocol;
import com.xylink.utils.SpringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by konglk on 2018/8/13.
 */
public class AuthServerHandler extends SimpleChannelInboundHandler<Protocol.CLogin> {

    private AuthService authService = SpringUtils.getBean(AuthService.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol.CLogin msg) throws Exception {
        ClientConnection c = ClientConnectionMap.getClientConnection(ctx.channel().attr(ClientConnection.netIdKey).get());
        if(c != null && authService.login(msg)) {
            ClientConnectionMap.buildSession(c, msg);
        }
    }
}
