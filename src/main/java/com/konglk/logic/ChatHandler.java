package com.konglk.logic;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.protobuf.ByteString;
import com.konglk.auth.AuthService;
import com.konglk.config.MqConfig;
import com.konglk.protobuf.Protocol;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by konglk on 2018/9/2.
 */
@Component
public class ChatHandler implements IMessageHandler {
    @Autowired
    private AuthService authService;

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, Protocol.ProtocolMessage message) {
        Protocol.CPrivateChat chat = message.getRequest().getChat();
        if(authService.isValidMsg(chat)){
            //服务器收到消息后会将消息转发并把这条成功消息推送给发送客户端
            switch (chat.getDataType()){
                case TXT:
                    if(chat.getContent().isEmpty())
                        return;
                    Protocol.CPrivateChat txtChat = Protocol.CPrivateChat.newBuilder().mergeFrom(chat).setTs(System.currentTimeMillis()).build();
                    rabbitTemplate.convertAndSend(MqConfig.MESSAGE_TOPIC, MqConfig.ROUTING_KEY, txtChat);
                    ctx.writeAndFlush(Protocol.ProtocolMessage.newBuilder().setResponse(Protocol.ProtocolMessage.TResponse.newBuilder().
                            setChat(txtChat).setRespType(Protocol.ProtocolMessage.RequestType.CHAT).build()));
                    break;
                case VOICE:
                case IMG:
                    ByteString bytes = chat.getContent();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes.toByteArray());
                    StorePath storePath = storageClient.uploadFile("group1", inputStream, (long) bytes.size(), chat.getExtName());
                    try {
                        Protocol.CPrivateChat imgChat =
                                Protocol.CPrivateChat.newBuilder().mergeFrom(chat).
                                        setContent(ByteString.copyFrom(storePath.getFullPath(), "utf8")).
                                        setTs(System.currentTimeMillis()).build();
                        rabbitTemplate.convertAndSend(MqConfig.MESSAGE_TOPIC, MqConfig.ROUTING_KEY, imgChat);
                        ctx.writeAndFlush(Protocol.ProtocolMessage.newBuilder().setResponse(Protocol.ProtocolMessage.TResponse.newBuilder().
                                setChat(imgChat).setRespType(Protocol.ProtocolMessage.RequestType.CHAT).build()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }
}
