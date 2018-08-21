package com.xylink.gate;

import com.xylink.protobuf.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by konglk on 2018/8/13.
 */
@Component
public class GateAuthConnection {
    @Value("${im.auth.ip}")
    private String ip;
    @Value("${im.auth.port}")
    private int port;

    public void startGateAuthConnection() {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel)
                            throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast(new ProtobufDecoder(Protocol.ProtocolMessage.getDefaultInstance()));
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast("GateAuthConnectionHandler", new GateAuthConnectionHandler());  //Auth -> gate
                    }
                });

        bootstrap.connect(ip, port);
    }
}
