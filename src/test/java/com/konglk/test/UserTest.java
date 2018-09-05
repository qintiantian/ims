package com.konglk.test;

import com.github.tobato.fastdfs.domain.FileInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.konglk.constants.ImsConstants;
import com.konglk.entity.*;
import com.konglk.mappers.ConversationDao;
import com.konglk.mappers.UnreadCountDao;
import com.konglk.service.ConversationService;
import com.konglk.service.MsgService;
import com.konglk.service.RelationshipService;
import com.konglk.service.UserService;
import com.konglk.utils.FastDfsUtils;
import com.konglk.utils.IdBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
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
        userVO.setUsername("树树");
        userVO.setNickname("小树");
        userVO.setCountry("ch");
        userVO.setCity("wuhan");

        userVO.setImgUrl("http://192.168.1.21/imgs/shushu.png");
        String pwd = "shushu";
        userVO.setCellphone("13477907304");
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
//        conversationDao.insertConversation(vo);
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
        msgVO.setConversationId(conversationService.getConversation("eb7687c6-da11-4d23-bc71-36c4a12b2247","78ad305d-226e-4155-93e2-357ce376a194").getConversationId());
        msgService.insertMsg(msgVO);
    }

    @Autowired
    private MsgService msgService;

    @Test
    public void selHistoryMsg() {
        System.out.println(msgService.selectHistoryMessageById("eb7687c6-da11-4d23-bc71-36c4a12b2247", "78ad305d-226e-4155-93e2-357ce376a194", "22cafd51-5737-4a48-9f58-348cbf241675",5, 1));
    }

    @Test
    public void selConversationId() {
        System.out.println(conversationService.getConversation("eb7687c6-da11-4d23-bc71-36c4a12b2247", "78ad305d-226e-4155-93e2-357ce376a194"));
    }

    @Autowired
    private UnreadCountDao unreadCountDao;
    @Test
    public void unreadCountTest() {
        List<ConversationVO> conversationVOS = conversationDao.selAllConversation();
        for(ConversationVO vo:conversationVOS) {
            //每个会话对应2个未读消息统计记录
            UnreadCountVO unreadVO = new UnreadCountVO();
            unreadVO.setConversationId(vo.getConversationId());
            unreadVO.setUserId(vo.getUserId());
            unreadVO.setUnreadCountId(IdBuilder.buildId());
            unreadVO.setUnreadCount(0);
            unreadCountDao.insertUnreadCount(unreadVO);

            unreadVO = new UnreadCountVO();
            unreadVO.setConversationId(vo.getConversationId());
            unreadVO.setUnreadCount(0);
            unreadVO.setUnreadCountId(IdBuilder.buildId());
            unreadVO.setUserId(vo.getDestId());
            unreadCountDao.insertUnreadCount(unreadVO);
        }
    }

    @Autowired
    private FastFileStorageClient client;

    @Test
    public void testUpload() throws FileNotFoundException {
        FileInfo fileInfo = client.queryFileInfo("group1", "M00/00/00/rBET9luKjWWAYY3AAAABPj7c3sE2681_big.sh");
        System.out.println(fileInfo);
        FileInputStream in = new FileInputStream("d:\\trackerd.log");
        StorePath storePath = client.uploadFile(in, 2702, "java", null);
        System.out.println(storePath);
    }

    @Autowired
    private RelationshipService relationshipService;

    @Test
    public void insertRelationship(){
        String fromUser = "eb7687c6-da11-4d23-bc71-36c4a12b2247";
        String toUser = "78ad305d-226e-4155-93e2-357ce376a194";
        relationshipService.insertRelationship(fromUser, toUser);
    }
}
