package com.konglk.service;

import com.konglk.common.DataProcess;
import com.konglk.entity.ConversationVO;
import com.konglk.entity.UnreadCountVO;
import com.konglk.mappers.ConversationDao;
import com.konglk.mappers.UnreadCountDao;
import com.konglk.utils.DateFormatter;
import com.konglk.utils.IdBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UnreadCountDao unreadCountDao;

    public void buildConversation(ConversationVO vo) {
        vo.setTs(System.currentTimeMillis());
        vo.setConversationId(IdBuilder.buildId());
        conversationDao.insertConversation(vo);

        //每个会话对应2个未读消息统计记录
        UnreadCountVO unreadVO = new UnreadCountVO();
        unreadVO.setConversationId(vo.getConversationId());
        unreadVO.setUserId(vo.getUserId());
        unreadVO.setUnreadCountId(IdBuilder.buildId());
        unreadVO.setUnreadCount(0);
        unreadCountDao.insertUnreadCount(unreadVO);

        unreadVO = new UnreadCountVO();
        unreadVO.setConversationId(vo.getConversationId());
        unreadVO.setUnreadCount(0);
        unreadVO.setUnreadCountId(IdBuilder.buildId());
        unreadVO.setUserId(vo.getDestId());
        unreadCountDao.insertUnreadCount(unreadVO);
    }

    public List<Map<String,Object>> getConversationByUserId(String userId) {
        List<Map<String,Object>> datas = conversationDao.selectConversationByUserId(userId);
        if(CollectionUtils.isEmpty(datas))
            return Collections.EMPTY_LIST;
        DataProcess.process(datas, new String[]{"lastDate"}, new Function[]{DateFormatter::format});
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
