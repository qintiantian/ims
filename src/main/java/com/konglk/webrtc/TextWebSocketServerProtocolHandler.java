package com.konglk.webrtc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.konglk.conn.ClientConnectionMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class TextWebSocketServerProtocolHandler extends SimpleChannelInboundHandler<Object> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private WebSocketServerHandshaker handshaker;
    public static Map<String, ChannelHandlerContext> webrtcConnection = new HashMap<>();
    private static AttributeKey<String> CONN_NAME = AttributeKey.valueOf("conn_name");
    private static AttributeKey<String> OTHER_NAME = AttributeKey.valueOf("other_name");


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("active a new channel " + ctx.channel().remoteAddress());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("remove a channel " + ctx.channel().remoteAddress());
        String otherName = ctx.channel().attr(OTHER_NAME).get();
        webrtcConnection.remove(ctx.channel().attr(CONN_NAME).get());
        ctx.channel().attr(CONN_NAME).set(null);
        if(!StringUtils.isEmpty(otherName)) {
            ChannelHandlerContext otherCtx = webrtcConnection.get(otherName);
            ctx.channel().attr(OTHER_NAME).set(null);
            if(otherCtx != null){
                JSONObject result = new JSONObject();
                result.put("type", "leave");
                otherCtx.writeAndFlush(new TextWebSocketFrame(result.toString()));
            }

        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) throws Exception {

        // 如果HTTP解码失败，返回HHTP异常
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1,
                    BAD_REQUEST));
            return;
        }

        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:3000", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame) {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(),
                    (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        JSONObject data = JSON.parseObject(request);
        String userId = data.getString("name");

        String type = data.getString("type");
        JSONObject result = new JSONObject();
        ChannelHandlerContext destCtx = null;
        switch (type) {
            case "login":
                logger.info("User logged {}",userId);
                if(!StringUtils.isEmpty(userId) && ClientConnectionMap.getClientConnection(userId) != null) {
                    webrtcConnection.put(userId, ctx);
                }
                result.put("type","login");
                if(ClientConnectionMap.getClientConnection(userId) != null) {
                    ctx.channel().attr(CONN_NAME).set(userId);
                    result.put("success", true);
                }else{
                    result.put("success", false);
                }
                ctx.writeAndFlush(new TextWebSocketFrame(result.toString()));
                break;
            case "call":
                result.put("type", "call");
                result.put("caller", data.getString("userId"));
                result.put("callee", data.getString("destId"));
                destCtx = webrtcConnection.get(data.getString("destId"));
                if(destCtx != null)
                    destCtx.writeAndFlush(new TextWebSocketFrame(result.toString()));
                break;
            case "offer":
                logger.info("Sending offer to {}", userId);
                destCtx = webrtcConnection.get(userId);
                if(destCtx != null) {
                    destCtx.channel().attr(OTHER_NAME).set(userId);
                    result.put("type", "offer");
                    result.put("offer", data.get("offer"));
                    result.put("name", ctx.channel().attr(CONN_NAME).get());
                    destCtx.channel().writeAndFlush(new TextWebSocketFrame(result.toString()));
                }
                break;
            case "answer":
                logger.info("Sending answer to {}", userId);
                destCtx = webrtcConnection.get(userId);
                if(destCtx != null) {
                    destCtx.channel().attr(OTHER_NAME).set(userId);
                    result.put("type","answer");
                    result.put("answer", data.get("answer"));
                    destCtx.channel().writeAndFlush(new TextWebSocketFrame(result.toString()));
                }
                break;
            case "candidate":
                logger.info("Sending candidate to {}", userId);
                destCtx = webrtcConnection.get(userId);
                if(destCtx != null) {
                    result.put("type","candidate");
                    result.put("candidate", data.get("candidate"));
                    destCtx.channel().writeAndFlush(new TextWebSocketFrame(result.toString()));
                }
                break;
            case "leave":
                logger.info("Disconneting from "+userId);
                webrtcConnection.remove(userId);
                destCtx = webrtcConnection.get(userId);
                if(destCtx != null) {
                    destCtx.channel().attr(OTHER_NAME).set(null);
                    result.put("type", "leave");
                    destCtx.writeAndFlush(new TextWebSocketFrame(result.toString()));
                }
                break;
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res, res.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }


}