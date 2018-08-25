package com.xylink.service;

import com.xylink.mappers.ConversationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by konglk on 2018/8/25.
 */
@Service
public class ConversationService {
    @Autowired
    private ConversationDao conversationDao;

    public List<Map<String,Object>> getConversationByUserId(String userId) {
        return conversationDao.selectConversationByUserId(userId);
    }
}
