package com.xylink.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by konglk on 2018/8/27.
 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "hello world";
    }

}
