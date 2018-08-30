package com.konglk.service;

import com.konglk.common.DataProcess;
import com.konglk.entity.ConversationVO;
import com.konglk.entity.MsgVO;
import com.konglk.enums.MsgConfig;
import com.konglk.mappers.MsgDao;
import com.konglk.protobuf.Protocol;
import com.konglk.utils.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * Created by konglk on 2018/8/24.
 */
@Service
public class MsgService {
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private ConversationService conversationService;


    public void insertMsg(MsgVO msgVO) {
        msgVO.setTs(System.currentTimeMillis());
        msgVO.setMsgId(UUID.randomUUID().toString());
        msgVO.setHasRead(MsgConfig.NOT_READ);
        msgDao.insertMsg(msgVO);
        ConversationVO conversationVO = conversationService.getConversation(msgVO.getSendId(), msgVO.getDestId());
        conversationService.updateLastDate(conversationVO.getConversationId());
    }

    public List<Map<String,Object>> selectHistoryMessageById(String sendId, String destId, String msgId, int pageSize, int direct){
        List<Map<String,Object>> datas =
                msgDao.selectHistoryMessageById(sendId, destId, msgId, pageSize, direct);
        if(CollectionUtils.isEmpty(datas))
            return Collections.EMPTY_LIST;
        DataProcess.process(datas, new String[]{"createtime"}, new Function[]{DateFormatter::format});
        return datas;

    }

    public MsgVO buildMsg(Protocol.CPrivateChat msg) {
        MsgVO msgVO = new MsgVO();
        try {
            msgVO.setContent(new String(msg.getContent().toByteArray(), "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        msgVO.setSendId(msg.getUserId());
        msgVO.setDestId(msg.getDestId());
        msgVO.setMsgId(UUID.randomUUID().toString());
        msgVO.setMsgType(msg.getDataType().getNumber());
        msgVO.setConversationId(conversationService.getConversation(msg.getUserId(), msg.getDestId()).getConversationId());
        return msgVO;
    }
}
