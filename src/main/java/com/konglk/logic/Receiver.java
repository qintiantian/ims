package com.konglk.logic;

import com.konglk.protobuf.Protocol;
import com.konglk.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by konglk on 2018/10/21.
 */
@Component
public class Receiver {
    @Autowired
    private MsgService msgService;
    @Autowired
    private MessageProcessor messageProcessor;

    public void receiveMessage(Protocol.CPrivateChat message) {
//        System.out.println("Received <" + message + ">");
        msgService.insertMsg(msgService.buildMsg(message));
        messageProcessor.process(message);
    }
}
