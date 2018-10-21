package com.konglk.service;

import com.konglk.common.DataProcess;
import com.konglk.constants.ImsConstants;
import com.konglk.entity.ConversationVO;
import com.konglk.entity.UnreadCountVO;
import com.konglk.mappers.ConversationDao;
import com.konglk.mappers.UnreadCountDao;
import com.konglk.utils.DateFormatter;
import com.konglk.utils.IdBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by konglk on 2018/8/25.
 */
@Service
public class ConversationService {
    @Autowired
    private ConversationDao conversationDao;
    @Autowired
    private MsgService msgService;

    public void buildConversation(ConversationVO vo) {
        vo.setTs(System.currentTimeMillis());
        vo.setConversationId(IdBuilder.buildId());
        conversationDao.insertConversation(vo);
    }

    /**
     * 获取用户的会话列表，包含最后一条消息、时间，以及未读消息条数，会话对方头像、信息
     * @param userId
     * @return
     */
    public List<Map<String,Object>> getConversationByUserId(String userId) {
        List<Map<String,Object>> datas = conversationDao.selectConversationByUserId(userId);
        if(CollectionUtils.isEmpty(datas))
            return Collections.EMPTY_LIST;
        DataProcess.process(datas, new String[]{"lastDate"}, new Function[]{DateFormatter::format});
        for(Map<String, Object> data : datas) {
            String destId = (String) data.get("destId");
            data.put("unreadCount", msgService.getUnreadCount(userId, destId));
        }
        return datas;
    }

    public void updateLastDate(String conversationId) {
        if(StringUtils.isEmpty(conversationId))
            return;
        Long cur = System.currentTimeMillis();
        conversationDao.updateConversationDate(cur, conversationId);
    }

    public ConversationVO getConversation(String sendId, String destId) {
        return conversationDao.selConversation(sendId, destId);
    }

}
