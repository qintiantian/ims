package com.konglk.logic;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.protobuf.ByteString;
import com.konglk.auth.AuthService;
import com.konglk.entity.MsgVO;
import com.konglk.protobuf.Protocol;
import com.konglk.service.MsgService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by konglk on 2018/9/2.
 */
@Component
public class ChatHandler implements IMessageHandler {
    @Autowired
    private MessageQueue messageQueue;
    @Autowired
    private AuthService authService;
    @Autowired
    private MsgService msgService;
    @Autowired
    private FastFileStorageClient storageClient;
    @Override
    public void process(ChannelHandlerContext ctx, Protocol.ProtocolMessage message) {
        Protocol.CPrivateChat chat = message.getRequest().getChat();
        if(authService.isValidMsg(chat)){
            switch (chat.getDataType()){
                case TXT:
                    messageQueue.push(chat);
                    msgService.insertMsg(msgService.buildMsg(chat));
                    break;
                case VOICE:
                case IMG:
                    MsgVO msgVO = msgService.buildMsg(chat);
                    ByteString bytes = chat.getContent();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes.toByteArray());
                    StorePath storePath = storageClient.uploadFile("group1", inputStream, (long) bytes.size(), chat.getExtName());
                    msgVO.setContent(storePath.getFullPath());
                    msgService.insertMsg(msgVO);
                    try {
//                        Protocol.CPrivateChat.newBuilder().setChatType(chat.getChatType()).setContent(ByteString.copyFrom(storePath.getFullPath(), "utf8"))
//                            .setDataType(chat.getDataType()).setTs(chat.getTs()).setDestId(chat.getDestId());
                        Protocol.CPrivateChat newChat =
                                Protocol.CPrivateChat.newBuilder().mergeFrom(chat).setContent(ByteString.copyFrom(storePath.getFullPath(), "utf8")).build();
                        messageQueue.push(newChat);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    messageQueue.push(chat);
                    break;
            }

        }
    }
}
