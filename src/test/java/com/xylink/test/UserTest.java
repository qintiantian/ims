package com.xylink.test;

import com.xylink.constants.ImsConstants;
import com.xylink.entity.ConversationVO;
import com.xylink.entity.MsgVO;
import com.xylink.entity.UserVO;
import com.xylink.mappers.ConversationDao;
import com.xylink.service.ConversationService;
import com.xylink.service.MsgService;
import com.xylink.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        userVO.setUsername("点点");
        userVO.setNickname("小点");
        userVO.setCountry("ch");
        userVO.setCity("wuhan");

        userVO.setImgUrl("http://192.168.1.21/imgs/diandian.png");
        String pwd = "diandian";
        userVO.setCellphone("13477907301");
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
    ConversationService conversationService;
    @Autowired
    ConversationDao conversationDao;

    @Test
    public void inserConversation(){
        ConversationVO vo = new ConversationVO();
        vo.setUserId("76a43666-9d38-48ea-b6b6-78a4488fe70e");
        vo.setDestId("78ad305d-226e-4155-93e2-357ce376a194");
        vo.setStatus(1);
        vo.setTs(System.currentTimeMillis());
        vo.setConversationId(UUID.randomUUID().toString());
        conversationDao.insertConversation(vo);
    }

    @Test
    public void selConversion() {
        System.out.println(conversationService.getConversationByUserId("eb7687c6-da11-4d23-bc71-36c4a12b2247"));
    }

    @Test
    public void insertMsg() {
        MsgVO msgVO = new MsgVO();
        msgVO.setSendId("eb7687c6-da11-4d23-bc71-36c4a12b2247");
        msgVO.setDestId("78ad305d-226e-4155-93e2-357ce376a194");
        msgVO.setContent("你好吗？");
    }

    @Autowired
    private MsgService msgService;

    @Test
    public void selHistoryMsg() {
        System.out.println(msgService.selectHistoryMessageById("eb7687c6-da11-4d23-bc71-36c4a12b2247", "78ad305d-226e-4155-93e2-357ce376a194", "b310147c-7405-413e-989b-5d1f3223f212",20));
    }

    @Test
    public void selConversationId() {
        System.out.println(conversationService.getConversation("eb7687c6-da11-4d23-bc71-36c4a12b2247", "78ad305d-226e-4155-93e2-357ce376a194"));
    }
}
