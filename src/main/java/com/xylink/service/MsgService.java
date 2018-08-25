package com.xylink.service;

import com.xylink.entity.MsgVO;
import com.xylink.enums.MsgConfig;
import com.xylink.mappers.MsgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by konglk on 2018/8/24.
 */
@Service
public class MsgService {
    @Autowired
    private MsgDao msgDao;

    public void insertMsg(MsgVO msgVO) {
        msgVO.setTs(System.currentTimeMillis());
        msgVO.setMsgId(UUID.randomUUID().toString());
        msgVO.setHasRead(MsgConfig.NOT_READ);
        msgDao.insertMsg(msgVO);
    }
}
