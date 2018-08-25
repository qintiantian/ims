package com.xylink.conn;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.security.cert.Certificate;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by konglk on 2018/8/11.
 */
public class ClientConnection {
    private static AtomicLong netIdGenerator = new AtomicLong();
    public static AttributeKey<Long> netIdKey = AttributeKey.valueOf("netId");

    private ChannelHandlerContext ctx;
    private String userId;
    private Long netId;
    private String certificate;

    public ClientConnection(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        netId = netIdGenerator.incrementAndGet();
        ctx.channel().attr(netIdKey).set(netId);
    }

    public void close() {
        this.ctx.channel().close();
    }

    public String getUserId() {
        return userId;
    }

    public Long getNetId() {
        return netId;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}
