package com.xylink.gate;

import com.xylink.protobuf.Chat;
import com.xylink.protobuf.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by konglk on 2018/8/11.
 */
@Component
public class GateServer implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${im.gate.port}")
    private int port;

    public void  startGateServer() throws InterruptedException {
        logger.info("start gate server at port " + port);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(10, 10, 30, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new ProtobufDecoder(Protocol.ProtocolMessage.getDefaultInstance()));
                            ch.pipeline().addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new GateServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //绑定并且准备接受消息
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{
            try {
                startGateServer();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }).start();
    }
}
