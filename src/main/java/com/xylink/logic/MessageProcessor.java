package com.xylink.logic;

import com.xylink.conn.ClientConnection;
import com.xylink.conn.ClientConnectionMap;
import com.xylink.protobuf.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by konglk on 2018/8/11.
 */
@Component
public class MessageProcessor {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private MessageQueue messageQueue;

    public void process(Protocol.CPrivateChat msg) {
        if (msg == null || StringUtils.isEmpty(msg.getDestId()))
            return;
        ClientConnection c = ClientConnectionMap.getClientConnection(msg.getDestId());
        if (c == null) {
//            logger.info("dest user "+msg.getDestId()+" not online");
            messageQueue.push2NotReadQueue(msg);
        } else {
            ChannelHandlerContext ctx = c.getCtx();
            Channel channel = ctx.channel();
            //TODO increase seqNum
            Protocol.ProtocolMessage m = Protocol.ProtocolMessage.newBuilder()
                    .setResponse(Protocol.ProtocolMessage.TResponse.newBuilder().setChat(msg).build())
                    .build();
            ChannelFuture channelFuture = channel.writeAndFlush(m);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    if (!future.isSuccess()) {
                        messageQueue.push2NotReadQueue(msg);
                        future.channel().close();
                    } else {
                        logger.info("user [" + msg.getUserId() + "] send msg to user [" + msg.getDestId() + "]");
                    }
                }
            });

        }
    }

    @PostConstruct
    public void consume() {
        Random r = new Random();
        int internal = 100;
        executorService.execute(() -> {
            while (true) {
                Protocol.CPrivateChat msg = messageQueue.pop();
                if (msg == null) {
                    try {
                        Thread.sleep(r.nextInt(internal));
                    } catch (InterruptedException e) {
                    }
                } else {
                    process(msg);
                }
            }
        });
    }

    public void handleNotReadMessage(String userId) {
        //用户上线后推送所有未读消息
        executorService.execute(() -> {
            Protocol.CPrivateChat msg = messageQueue.notReadPop(userId);
            while (msg != null) {
                process(msg);
                msg = messageQueue.notReadPop(userId);
            }
        });
    }
}
