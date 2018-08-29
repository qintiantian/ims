package com.xylink.mappers;

import com.xylink.entity.ConversationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    @Update("update ims_conversation set last_date=#{newDate} where conversation_id=#{conversationId}")
    void updateConversationDate(@Param("newDate") Long newDate, @Param("conversationId") String conversationId);

    ConversationVO selConversation(@Param("sendId") String sendId, @Param("destId") String destId );
}
