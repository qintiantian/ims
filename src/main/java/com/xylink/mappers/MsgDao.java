package com.xylink.mappers;

import com.xylink.entity.MsgVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    List<Map<String, Object>> selectHistoryMessageById(@Param("sendId") String sendId, @Param("destId") String destId);
}
