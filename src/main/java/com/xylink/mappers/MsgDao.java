package com.xylink.mappers;

import com.xylink.entity.MsgVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by konglk on 2018/8/24.
 */
@Mapper
@Component("msgDao")
public interface MsgDao {
    void insertMsg(MsgVO msgVO);
}
