package com.konglk.service;

import com.konglk.entity.UserVO;
import com.konglk.enums.UserConfig;
import com.konglk.mappers.UserDao;
import com.konglk.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.util.Map;
import java.util.UUID;

/**
 * Created by konglk on 2018/8/24.
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public void insertUser(UserVO userVO) {
        userVO.setSugar(Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()));
        String pwd = userVO.getPwd();
        String sugar = userVO.getSugar();
        userVO.setPwd(EncryptUtils.crypt(sugar+pwd));
        userVO.setCreatetime(System.currentTimeMillis());
        userVO.setUpdatetime(System.currentTimeMillis());
        userVO.setStatus(UserConfig.UserStatus.NORMAL.v);
        userDao.insertUser(userVO);
    }

    public UserVO selectUser(String unique) {
        return userDao.selectUser(unique);
    }

    public void updatePwd(String newpwd, String unique) {
        UserVO userVO = userDao.selectUser(unique);
        if(userVO != null) {
            String encrptpwd = EncryptUtils.crypt(userVO.getSugar()+newpwd);
            userDao.updatePwd(encrptpwd, unique);
        }
    }

    public Map<String,Object> selecUserById(String userId) {
        return userDao.selectUserById(userId);
    }

}
