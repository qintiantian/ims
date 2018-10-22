package com.konglk.service;

import com.konglk.common.DataProcess;
import com.konglk.constants.ImsConstants;
import com.konglk.entity.ConversationVO;
import com.konglk.entity.MsgVO;
import com.konglk.enums.MsgConfig;
import com.konglk.mappers.MsgDao;
import com.konglk.protobuf.Protocol;
import com.konglk.utils.DateFormatter;
import com.konglk.utils.IdBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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
    private RedisTemplate redisTemplate;
    @Autowired
    private ConversationService conversationService;


    public void insertMsg(MsgVO msgVO) {
        msgVO.setMsgId(UUID.randomUUID().toString());
        msgDao.insertMsg(msgVO);
        ConversationVO conversationVO = conversationService.getConversation(msgVO.getSendId(), msgVO.getDestId());
        conversationService.updateLastDate(conversationVO.getConversationId());
    }

    public List<Map<String,Object>> selectHistoryMessageById(String sendId, String destId, String msgId, int pageSize, int direct){
        List<Map<String,Object>> datas =
                msgDao.selectHistoryMessageById(sendId, destId, msgId, pageSize, direct);
        if(CollectionUtils.isEmpty(datas))
            return Collections.EMPTY_LIST;
//        DataProcess.process(datas, new String[]{"createtime"}, new Function[]{DateFormatter::format});
        return datas;

    }

    public MsgVO buildMsg(Protocol.CPrivateChat msg) {
        MsgVO msgVO = new MsgVO();
        try {
            if(msg.getDataType() == Protocol.CPrivateChat.DataType.TXT)
                msgVO.setContent(new String(msg.getContent().toByteArray(), "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        msgVO.setSendId(msg.getUserId());
        msgVO.setDestId(msg.getDestId());
        msgVO.setMsgId(IdBuilder.buildId());
        msgVO.setMsgType(msg.getDataType().getNumber());
        msgVO.setTs(msg.getTs()==0 ? System.currentTimeMillis() : msg.getTs());
        msgVO.setHasRead(MsgConfig.NOT_READ);
        msgVO.setConversationId(conversationService.getConversation(msg.getUserId(), msg.getDestId()).getConversationId());
        return msgVO;
    }

    public List<String> selectImagesById(String sendId, String destId) {
        List<String> images = msgDao.selectImagesById(sendId, destId);
        if(CollectionUtils.isEmpty(images))
            return Collections.emptyList();
        return images;
    }

    public void notifyReaded(String userId, String destId) {
        if(userId == null || destId == null)
            return;
        redisTemplate.opsForHash().delete(ImsConstants.IMS_UNREAD_COUNT+userId, destId);
    }

    public void increment(String userId, String destId, long delta) {
        if(userId == null || destId == null)
            return;
        redisTemplate.opsForHash().increment(ImsConstants.IMS_UNREAD_COUNT+userId, destId, delta);
    }

    public long getUnreadCount(String userId, String destId) {
        Object c = redisTemplate.opsForHash().get(ImsConstants.IMS_UNREAD_COUNT+userId, destId);
        if(c==null)
            return 0L;
        try {
            return Long.parseLong(c.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
