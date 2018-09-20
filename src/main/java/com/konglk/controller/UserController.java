package com.konglk.controller;

import com.konglk.conn.ClientConnection;
import com.konglk.conn.ClientConnectionMap;
import com.konglk.data.UserData;
import com.konglk.mappers.UserDao;
import com.konglk.service.ConversationService;
import com.konglk.service.MsgService;
import com.konglk.service.RelationshipService;
import com.konglk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by konglk on 2018/8/25.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MsgService msgService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private RelationshipService relationshipService;

    @GetMapping("/profile/{userId}/{certificate}")
    public Object userProfile(@PathVariable("userId")String userId, @PathVariable("certificate")String certificate) {
        Map<String,Object> userVO = userService.selecUserById(userId);
        if(userVO == null) {
            return "";
        }
        ClientConnection c = ClientConnectionMap.getClientConnection(userId);
        if(c == null || StringUtils.isEmpty(c.getCertificate()))
            return "";
        if(c.getCertificate().equals(certificate)) {
            return userVO;
        }
        return "";
    }

    @GetMapping("/conversation/{userId}")
    public Object conversationList(@PathVariable String userId){
        return conversationService.getConversationByUserId(userId);
    }

    @GetMapping("/historymessage/{userId}/{destId}/{msgId}/{pageSize}")
    public Object historyMessage(@PathVariable String userId, @PathVariable String destId, @PathVariable String msgId,
                                 @PathVariable int pageSize, @RequestParam("direct") int direct){
        List<Integer> valid = Arrays.asList(1,2,-1,-2);
        if(valid.contains(direct))
            return msgService.selectHistoryMessageById(userId, destId, msgId, pageSize, direct);
        return "";
    }

    @GetMapping("/historymessage/{userId}/{destId}/images")
    public Object messageImages(@PathVariable String userId, @PathVariable String destId) {
        return msgService.selectImagesById(userId, destId);
    }

    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/relationships")
    public Object findUserRelationships(@PathVariable("userId")String userId) {
        Map<String, List<UserData>> relationshipMap = relationshipService.selectRelationshipByUserId(userId);

        return !relationshipMap.isEmpty() ? relationshipMap : Collections.emptyMap();
    }

    /**
     * 查询新朋友
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/relationships/new")
    public Object findUserNewFriends(@PathVariable("userId")String userId) {
        List<UserData> newFriendsList = relationshipService.selectNewFriendsByUserId(userId);

        return !newFriendsList.isEmpty() ? newFriendsList : Collections.emptyList();
    }

    /**
     * 添加好友
     * @param userId
     * @param toUser
     * @return
     */
    @PostMapping("/{userId}/relationships")
    public Object insertRelationship(@PathVariable("userId") String userId, String toUser) {
        relationshipService.insertRelationship(userId, toUser);
        return "";
    }

    /**
     * 通过好友验证
     * @param userId
     * @param fromUser
     * @return
     */
    @PostMapping("/{userId}/relationships/{fromUser}/pass")
    public Object passRelationship(@PathVariable("userId") String userId, @PathVariable("fromUser") String fromUser){
        return this.relationshipService.passRelationship(fromUser, userId);
    }

    /**
     * 删除好友
     * @param userId
     * @param toUser
     * @return
     */
    @DeleteMapping("/{userId}/relationships/{toUser}/delete")
    public Object delRelationship(@PathVariable("userId") String userId, @PathVariable("toUser") String toUser){
        this.relationshipService.delRelationship(userId, toUser);
        return "";
    }

    /**
     * 查询好友详情
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/relationships/{toUser}/detail")
    public Object getFriendDetail(@PathVariable("userId")String userId, @PathVariable("toUser") String toUser) {
        Map<String, Object> friend = userService.selecUserById(toUser);
        return friend;
    }

    @GetMapping("/test")
    public Object test() { return "hello world"; }
}
