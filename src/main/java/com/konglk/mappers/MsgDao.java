package com.konglk.mappers;

import com.konglk.entity.MsgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by konglk on 2018/8/24.
 */
@Mapper
@Component("msgDao")
public interface MsgDao {
    void insertMsg(MsgVO msgVO);
    List<Map<String, Object>> selectHistoryMessageById(@Param("sendId") String sendId, @Param("destId") String destId, @Param("msgId")String msgId, @Param("pageSize")int pageSize, @Param("direct")int direct);
    @Select("select m.content from ims_msg m where ((m.send_id=#{sendId} and m.dest_id=#{destId}) or (m.send_id = #{destId} and m.dest_id=#{sendId})) and m.msg_type = 3 order by m.ts")
    List<String> selectImagesById(@Param("sendId") String sendId, @Param("destId") String destId);
}