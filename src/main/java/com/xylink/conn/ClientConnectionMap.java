package com.xylink.conn;

import com.xylink.protobuf.Chat;
import com.xylink.protobuf.Protocol;
import com.xylink.utils.EncryptUtils;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by konglk on 2018/8/11.
 */
public class ClientConnectionMap {
    private static Logger logger = LoggerFactory.getLogger(ClientConnectionMap.class);
    private static ConcurrentHashMap<Long, ClientConnection> allClientConnectionMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Long> userId2NetIdMap = new ConcurrentHashMap<>();

    public static void addClientConnection(ChannelHandlerContext ctx) {
        ClientConnection c = new ClientConnection(ctx);
        if (allClientConnectionMap.putIfAbsent(ctx.channel().attr(ClientConnection.netIdKey).get(), c) != null) {
            logger.warn("duplicate registry");
        }
    }

    public static void removeClientConnection(ChannelHandlerContext ctx) {
        Long netId = ctx.channel().attr(ClientConnection.netIdKey).get();
        ClientConnection c = allClientConnectionMap.get(netId);
        String userId = c.getUserId();
        if(!StringUtils.isEmpty(userId))
            userId2NetIdMap.remove(userId);
        allClientConnectionMap.remove(netId);
    }

    public static ClientConnection getClientConnection(Long netId) {
        ClientConnection clientConnection = allClientConnectionMap.get(netId);
        if (clientConnection != null)
            return clientConnection;
        return null;
    }

    public static ClientConnection getClientConnection(String userId) {
        Long netId = userId2NetIdMap.get(userId);
        if (netId == null)
            return null;
        ClientConnection c = allClientConnectionMap.get(netId);
        if (c == null) {
            userId2NetIdMap.remove(userId);
        }
        return c;
    }

    public static void buildSession(ClientConnection c, String userId) {
        c.setUserId(userId);
        if(userId2NetIdMap.contains(userId)) {
            long existNetId = userId2NetIdMap.get(userId);
            ClientConnection existConn = allClientConnectionMap.get(existNetId);
            if(existConn != null) {
                allClientConnectionMap.remove(existNetId);//踢掉上一个在线的
                existConn.close();
            }
        }
        userId2NetIdMap.put(userId, c.getNetId());
        c.setCertificate(EncryptUtils.base64Encode(UUID.randomUUID().toString()));
    }
}
