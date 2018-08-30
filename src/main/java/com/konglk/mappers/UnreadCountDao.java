package com.konglk.mappers;

import com.konglk.entity.UnreadCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * Created by konglk on 2018/8/30.
 */
@Mapper
@Component
public interface UnreadCountDao {
    void insertUnreadCount(UnreadCountVO vo);

    @Update("update ims_unread_count set unread_count=unread_count+1 where conversation_id=#{conversationId} and user_id=#{userId}")
    void increaseCount(String conversationId, String userId);

}
