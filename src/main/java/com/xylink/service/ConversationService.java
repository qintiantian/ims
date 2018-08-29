package com.xylink.service;

import com.xylink.common.DataProcess;
import com.xylink.entity.ConversationVO;
import com.xylink.mappers.ConversationDao;
import com.xylink.utils.DateFormatter;
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
