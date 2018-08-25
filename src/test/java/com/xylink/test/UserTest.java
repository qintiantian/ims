package com.xylink.test;

import com.xylink.constants.ImsConstants;
import com.xylink.entity.ConversationVO;
import com.xylink.entity.UserVO;
import com.xylink.mappers.ConversationDao;
import com.xylink.mappers.UserDao;
import com.xylink.service.UserService;
import com.xylink.utils.EncryptUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Base64Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by konglk on 2018/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @Test
    public void insertUsers() {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> users = new HashMap<>();
        users.put("konglk", "konglk");
        users.put("qintian", "qintian");
        users.put("wumeng", "wumeng");
        hashOps.putAll(ImsConstants.IMS_USERS, users);

        System.out.println((hashOps.get(ImsConstants.IMS_USERS, "konglk")));
    }

    @Test
    public void insert2DB() {
        UserVO userVO = new UserVO();
        userVO.setUserId(UUID.randomUUID().toString());
        userVO.setUsername("秦田");
        userVO.setNickname("左耳");
        userVO.setCountry("ch");
        userVO.setCity("wuhan");

        userVO.setImgUrl("http://localhost/imgs/qintian.png");
        String pwd = "qintian";
        userVO.setCellphone("18062742155");
        userVO.setPwd(pwd);
        userService.insertUser(userVO);
    }

    @Test
    public void selUser() {
        UserVO userVO = userService.selectUser("左耳");
        System.out.println(userVO);
        System.out.println(userService.selectUser("180627438201")==null);
    }

    @Test
    public void updatePwd(){
        userService.updatePwd("konglk", "18062743820");
    }

    @Test
    public void selUserById() {
        Map<String,Object> userVO = userService.selecUserById("78ad305d-226e-4155-93e2-357ce376a194");
        System.out.println(userVO);
    }
    @Autowired
    ConversationDao conversationDao;

    @Test
    public void inserConversation(){
        ConversationVO vo = new ConversationVO();
        vo.setUserId("eb7687c6-da11-4d23-bc71-36c4a12b2247");
        vo.setDestId("78ad305d-226e-4155-93e2-357ce376a194");
        vo.setStatus(1);
        vo.setTs(System.currentTimeMillis());
        vo.setConversationId(UUID.randomUUID().toString());
        conversationDao.insertConversation(vo);
    }

    @Test
    public void selConversion() {
        System.out.println(conversationDao.selectConversationByUserId("eb7687c6-da11-4d23-bc71-36c4a12b2247"));
    }
}
