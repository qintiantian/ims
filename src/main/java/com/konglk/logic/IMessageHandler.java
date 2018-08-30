package com.konglk.logic;

import com.konglk.protobuf.Protocol;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by konglk on 2018/8/25.
 */
public interface IMessageHandler {
    void process(ChannelHandlerContext ctx, Protocol.ProtocolMessage message);
}
