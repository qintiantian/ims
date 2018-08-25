package com.xylink.mappers;

import com.xylink.entity.ConversationVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by konglk on 2018/8/25.
 */
@Mapper
@Component("conversationDao")
public interface ConversationDao {

    void insertConversation(ConversationVO vo);
    List<Map<String, Object>> selectConversationByUserId(String userId);
}
