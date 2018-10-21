package com.konglk.controller;

import com.konglk.constants.ImsConstants;
import com.konglk.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by konglk on 2018/10/21.
 */
@RestController
@RequestMapping("/msg")
public class MessageController {
    @Autowired
    private MsgService msgService;

    @GetMapping("/notifyReaded")
    public Object notifyReaded(@RequestParam String userId, @RequestParam String destId) {
        msgService.notifyReaded(userId, destId);
        return ImsConstants.SUCCESS_CODE;
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

}
