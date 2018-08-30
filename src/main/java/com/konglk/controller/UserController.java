package com.konglk.controller;

import com.konglk.conn.ClientConnection;
import com.konglk.conn.ClientConnectionMap;
import com.konglk.service.ConversationService;
import com.konglk.service.MsgService;
import com.konglk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @GetMapping("/test")
    public Object test() {
        return "hello world";
    }
}
