package com.konglk.controller;

import com.konglk.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by konglk on 2018/10/21.
 */
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/conversationList/{userId}")
    public Object conversationList(@PathVariable String userId){
        return conversationService.getConversationByUserId(userId);
    }

}
