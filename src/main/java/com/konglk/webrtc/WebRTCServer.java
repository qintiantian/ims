package com.konglk.webrtc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Created by konglk on 2018/9/9.
 */
@Component
public class WebRTCServer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${im.webrtc.port}")
    public int port;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private void  startWebRTCServer() throws InterruptedException {
        logger.info("start webrtc server at port " + port);

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(65536));
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new TextWebSocketServerProtocolHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        //绑定并且准备接受消息
        ChannelFuture f = b.bind(port).sync();
        f.channel().closeFuture().sync();
    }

    @PreDestroy
    private void shutdown() {
        logger.info(("###########shutdown webrtc####################"));
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public void start(){
        taskExecutor.execute(()->{
            try {
                startWebRTCServer();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
